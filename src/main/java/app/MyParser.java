package app;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
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
	
//	public static final String projectPath = "C:\\Users\\zakarea.alshara\\osgi_workspace\\projectToParse";
	public static final String projectPath = "/home/tony/M2/evoLog/evoLog_TP1";
//	public static final String projectSourcePath = projectPath + "\\src";
	public static final String projectSourcePath = projectPath + "/src";
//	public static final String jrePath = "C:\\Program Files\\Java\\jre1.8.0_51\\lib\\rt.jar";
//	public static final String jrePath = "/usr/bin/java";
	public static final String jrePath = "/usr/lib/jvm/java-8-openjdk-amd64/jre";
	public static void main(String[] args) throws IOException {

		// read java files
		String tmp = "src/main/java/dummy";
//		final File folder = new File(projectSourcePath);
		final File folder = new File(tmp);
		ArrayList<File> javaFiles = listJavaFilesForFolder(folder);

		//
		int nbClass = 0;
		int nbLine = 0;
		int nbMethod = 0;
		Set<String> packageNameSet = new HashSet<>();
		int nbLineMethod = 0;
		int nbAttrTotal = 0;
		for (File fileEntry : javaFiles) {
			String content = FileUtils.readFileToString(fileEntry);
			// System.out.println(content);

			CompilationUnit parse = parse(content.toCharArray());
			
			nbClass += getClassesNumber(parse);
			nbLine += getLinesCount(parse);
			nbMethod += getMethodCount(parse);
			packageNameSet.add(getPackageCount(parse));
			nbLineMethod += getLinesMethodCount(parse);
			nbAttrTotal += getAttrCount(parse);
			
		}
		float nbMoyenMeth = nbMethod/nbClass;
		float avgSizeMetho = nbLineMethod/nbMethod;

		System.out.println("Nombre de classes dans l'application: \n"+nbClass);
		System.out.println("Nombre de lignes dans l'application: \n"+nbLine);
		System.out.println("Nombre de méthodes dans l'application: \n"+nbMethod);
		System.out.println("Nombre de pacakge dans l'application: \n"+packageNameSet.size());
		System.out.println("Nombre moyen de méthode par class dans l'application: \n"+nbMoyenMeth);
		System.out.println("Taille moyennes des méthodes dans l'application: \n"+avgSizeMetho);
		System.out.println("Nombre d'attribut dans l'application: \n"+nbAttrTotal);

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
		MethodCountVisitor visitor = new MethodCountVisitor();
		parse.accept(visitor);

		return visitor.getNbMethods();
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
