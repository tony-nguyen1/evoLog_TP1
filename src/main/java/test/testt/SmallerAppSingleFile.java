package test.testt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import dummy.B;
import visitor.ASTVisitorMethodsName;
import visitor.ASTVisitorClassName;
import visitor.ASTVisitorSuperClassesName;

/**
 * Hello world!
 */
public class SmallerAppSingleFile {
	
	/**
	 * 
	 * @param path
	 * @return le contenu du fichier
	 */
	public static String getSource(String path) {
		Path filePath = Paths.get(path);

        try {
        	// Lire toutes les lignes du fichier en une liste de chaînes
        	List<String> lines = Files.readAllLines(filePath);
        	
        	// Joindre les lignes ensemble pour former une seule chaîne
            String content = String.join("\n", lines);
            
//            System.out.println(content);
            
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return null;
	}
    public static void main(String[] args) {
        System.out.println("Helllo World!");
        
        
   
        @SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS3);
        parser.setSource(getSource("src/main/java/dummy/A.java").toCharArray());
//        String source = "public class A extends B {\n"
//        		+ "	int i = 9;\n"
//        		+ "	public int j;\n"
//        		+ "	private ArrayList<Integer> al = new ArrayList<Integer>();\n"
//        		+ "	\n"
//        		+ "	@SuppressWarnings(\"unused\")\n"
//        		+ "	private boolean isBool(boolean b) {\n"
//        		+ "		return true;\n"
//        		+ "	}\n"
//        		+ "}";
//        System.out.println(source);
//        parser.setSource(source.toCharArray());
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        
        cu.accept(new ASTVisitorMethodsName());
        

        System.out.println("Goodbye World!");
    }
}
