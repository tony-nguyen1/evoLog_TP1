package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.internal.utils.FileUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import step2.MethodDeclarationVisitor;
import step2.MethodInvocationVisitor;
import step2.VariableDeclarationFragmentVisitor;

public class MyParser {
	
	static class CoupleNomData implements Comparable {
		
		private String nom;
		private int nb;
		
		

		public CoupleNomData(String nom, int nb) {
			super();
			this.nom = nom;
			this.nb = nb;
		}

		


		@Override
		public int hashCode() {
			return Objects.hash(nom);
		}




		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof CoupleNomData)) {
				return false;
			}
			CoupleNomData other = (CoupleNomData) obj;
			return Objects.equals(nom, other.nom);
		}




		@Override
		public int compareTo(Object o) {
			if (o instanceof CoupleNomData) {
				CoupleNomData tmp = (CoupleNomData) o;
				return tmp.nb - this.nb;
			}
			return 0;
		}
		
	}
	
//	public static final String projectPath = "C:\\Users\\zakarea.alshara\\osgi_workspace\\projectToParse";
	public static final String projectPath = "/home/tony/M2/evoLog/evoLog_TP1";
//	public static final String projectSourcePath = projectPath + "\\src";
	public static final String projectSourcePath = projectPath + "/src";
//	public static final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_51\\lib\\rt.jar";
//	public static final String jrePath = "/usr/bin/java";
	public static final String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";
	public static final int X = 2;
	
	private LineCountVisitor liveCountVisitor;

	public static void main(String[] args) throws IOException {

		// read java files
		String tmp = "src/main/java/dummy";
//		final File folder = new File(projectSourcePath);
		final File folder = new File(tmp);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);
		int nbFile = javaFiles.size();
		int selectionSize = (int) Math.ceil((double)nbFile/10);

		//
		int nbClass = 0;
		int nbLine = 0;
		int nbMethodTotal = 0;
		Set<String> packageNameSet = new HashSet<>();
		int nbLineMethod = 0;
		int nbAttrTotal = 0;
		ArrayList<CoupleNomData> listTopNbMethod = new ArrayList<>();
		ArrayList<CoupleNomData> listTopNbAttr = new ArrayList<>();
		HashSet<CoupleNomData> setTopNbMethod = new HashSet<>();
		HashSet<CoupleNomData> setTopNbAttr= new HashSet<>();
		ArrayList<CoupleNomData> listTopParam= new ArrayList<>();
		for (File fileEntry : javaFiles) {
			int nbMethod, nbAttr;
			String nom = fileEntry.getName();
			int end = nom.lastIndexOf(".java");
			nom = nom.substring(0, end);
//			System.out.println(nom);
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());
			
			// compute here for each file
			ClassesNumberVisitor visitor = new ClassesNumberVisitor();
			parse.accept(visitor);

			int sum = 0;
			for (TypeDeclaration method : visitor.getTypes()) {
				sum++;
			}
			
			nbClass += sum;
			nbLine += getLinesCount(parse);
			nbMethod = getMethodCount(parse);
			nbMethodTotal += nbMethod;
			packageNameSet.add(getPackageCount(parse));
			nbLineMethod += getLinesMethodCount(parse);
			nbAttr = getAttrCount(parse);
			nbAttrTotal += nbAttr;
			
			listTopParam.add(getMaxMethodNbParam(parse));
			listTopNbMethod.add(new CoupleNomData(nom, nbMethod));
			listTopNbAttr.add(new CoupleNomData(nom, nbAttr));
			
			printMethodInvocationInfo(parse);
			
		}
		float nbMoyenMeth = nbMethodTotal/nbClass;
		float avgSizeMetho = nbLineMethod/nbMethodTotal;
		Collections.sort(listTopNbMethod);
		for (int i=0; i<selectionSize; i++) {
//			System.out.println(listTopNbMethod.get(i).nom + listTopNbMethod.get(i).nb);
		}
		Collections.sort(listTopNbAttr);
		for (int i=0; i<selectionSize; i++) {
			setTopNbAttr.add(listTopNbAttr.get(i));
			setTopNbMethod.add(listTopNbMethod.get(i));
		}
		Collections.sort(listTopParam);
//		for(CoupleNomData c : listTopParam) {
//			System.out.println(c.nom+" "+c.nb);
//		}

		System.out.println("Nombre de classes dans l'application: \n"+nbClass);
		System.out.println("Nombre de lignes dans l'application: \n"+nbLine);
		System.out.println("Nombre de méthodes dans l'application: \n"+nbMethodTotal);
		System.out.println("Nombre de pacakge dans l'application: \n"+packageNameSet.size());
		System.out.println("Nombre moyen de méthode par class dans l'application: \n"+nbMoyenMeth);
		System.out.println("Taille moyennes des méthodes dans l'application: \n"+avgSizeMetho);
		System.out.println("Nombre d'attribut dans l'application: \n"+nbAttrTotal);
		System.out.println("Top 10% ("+selectionSize+"/"+nbFile+") des classes qui possèdent le plus de méthodes:");
		for (int i=0; i<selectionSize; i++) {
			System.out.print(listTopNbMethod.get(i).nom+" ");
		}System.out.println();
		System.out.println("Top 10% ("+selectionSize+"/"+nbFile+") des classes qui possèdent le plus d'attributs:");
		for (int i=0; i<selectionSize; i++) {
			System.out.print(listTopNbAttr.get(i).nom+" ");
		}System.out.println();
		System.out.println("L'intersection des 2:");
		setTopNbAttr.retainAll(setTopNbMethod);
		for(CoupleNomData c : setTopNbAttr) {
			System.out.println(c.nom);
		}
		System.out.println("Nombre maximal de paramètres pour une méthode de l'application:");
		if (!listTopParam.isEmpty()) {
			System.out.println(listTopParam.get(0).nom);
		}
	}
	
	public static void printMethodInvocationInfo(CompilationUnit parse) {

		MethodDeclarationVisitor visitor1 = new MethodDeclarationVisitor();
		parse.accept(visitor1);
		for (MethodDeclaration method : visitor1.getMethods()) {

			MethodInvocationVisitor visitor2 = new MethodInvocationVisitor();
			
			method.accept(visitor2);

			HashSet<String> setMethod = new HashSet();
			for (MethodInvocation methodInvocation : visitor2.getMethods()) {
//				System.out.println("method " + method.getName() + " invoc method "
//						+ methodInvocation.getName());
				
				setMethod.add(methodInvocation.getName().getIdentifier());
			}
			if (setMethod.isEmpty()) {
				System.out.println(method.getName() + " n'appel pas de méthode");
			} else {				
				System.out.println(method.getName() + " appel : ");		
				for (String s : setMethod) {
					System.out.println("  "+s);					
				}
			}

		}
	}
	
	
	
	// 
	public static int getClassesNumber(CompilationUnit parse) {
		ClassesNumberVisitor visitor = new ClassesNumberVisitor();
		parse.accept(visitor);

		int sum = 0;
		for (TypeDeclaration method : visitor.getTypes()) {
			sum++;
		}
		return sum;
	}
	
	// 
	public static int getLinesCount(CompilationUnit parse) {
		LineCountVisitor visitor = new LineCountVisitor(parse);
		parse.accept(visitor);

		return visitor.getTotalLines();
	}
	
	public static int getLinesMethodCount(CompilationUnit parse) {
		LineCountVisitor visitor = new LineCountVisitor(parse);
		parse.accept(visitor);

		return visitor.getNbMethodLine();
	}
	
	public static int getMethodCount(CompilationUnit parse) {
		LineCountVisitor visitor = new LineCountVisitor(parse);
		parse.accept(visitor);

		return visitor.getNbMethod();
	}
	
	public static String getPackageCount(CompilationUnit parse) {
		PackageCountVisitor visitor = new PackageCountVisitor();
		parse.accept(visitor);

		return visitor.getPackageName();
	}
	
	public static int getAttrCount(CompilationUnit parse) {
		LineCountVisitor visitor = new LineCountVisitor(parse);
		parse.accept(visitor);

		return visitor.getNbAttr();
	}
	
	public static CoupleNomData getMaxMethodNbParam(CompilationUnit parse) {
		LineCountVisitor visitor = new LineCountVisitor(parse);
		parse.accept(visitor);
		
		ArrayList<MyParser.CoupleNomData> l = visitor.getList();
		Collections.sort(l);

		return l.get(0);
	}
	
	

	// read all java files from specific folder
	public static ArrayList<File> listJavaFilesForFolder(final File folder) {
		ArrayList<File> javaFiles = new ArrayList<File>();
		for (File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				javaFiles.addAll(listJavaFilesForFolder(fileEntry));
			} else if (fileEntry.getName().contains(".java")) {
				// System.out.println(fileEntry.getName());
				javaFiles.add(fileEntry);
			}
		}

		return javaFiles;
	}

	// create AST
	private static CompilationUnit parse(char[] classSource) {
		ASTParser parser = ASTParser.newParser(AST.JLS4); // java +1.6
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
 
		parser.setBindingsRecovery(true);
 
		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);
 
		parser.setUnitName("");
 
		String[] sources = { projectSourcePath }; 
		String[] classpath = {jrePath};
 
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
		parser.setSource(classSource);
		
		return (CompilationUnit) parser.createAST(null); // create and parse
	}
}
