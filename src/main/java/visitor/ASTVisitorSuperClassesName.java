package visitor;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import test.testt.SmallerAppSingleFile;

public class ASTVisitorSuperClassesName extends ASTVisitor {
	@Override
	public boolean visit(TypeDeclaration node) {
		if (!node.isInterface()) {  // On s'assure que c'est une classe
            System.out.println("Class: " + node.getName());
            // Récupérer et afficher les super-classes
            printSuperClasses(node);
        }
        return super.visit(node);
	}

	// Fonction récursive pour afficher toutes les super-classes
    private void printSuperClasses(TypeDeclaration node) {
        // Récupérer la super-classe immédiate
        Type superClassType = node.getSuperclassType();
        
        if (superClassType != null) {
            System.out.println("Superclass: " + superClassType);
            
            // Si on veut continuer à remonter dans la hiérarchie
            // Il faut récupérer le TypeDeclaration de cette super-classe
            String superClassName = superClassType.toString();

            // Simuler la récupération du code source de la super-classe
            String superClassSourceCode = getSourceCodeForClass(superClassName);

            if (superClassSourceCode != null) {
                // Créer un parser pour analyser la super-classe
                ASTParser parser = ASTParser.newParser(AST.JLS3); // Ajuster selon la version JLS
                parser.setSource(superClassSourceCode.toCharArray());
                parser.setKind(ASTParser.K_COMPILATION_UNIT);

                // Créer un nouvel AST pour la super-classe et la visiter
                CompilationUnit cu = (CompilationUnit) parser.createAST(null);
                cu.accept(new ASTVisitor() {
                    @Override
                    public boolean visit(TypeDeclaration superClassNode) {
                        // Appel récursif pour explorer les super-classes
                        printSuperClasses(superClassNode);
                        return false; // Arrêter la visite après avoir traité la super-classe
                    }
                });
            }
        } else {
            System.out.println("No more superclasses (reached java.lang.Object or no superclass declared).");
        }
    }
    private String getSourceCodeForClass(String className) {
        // Simuler la récupération du code source
        // En pratique, tu devrais lire le fichier Java correspondant
    	
    	
    	String s = SmallerAppSingleFile.getSource("src/main/java/dummy/"+className+".java");
    	
    	if (s != null) {
    		return s;
    	}
    	
        return null;  // À implémenter selon ton contexte
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
}
