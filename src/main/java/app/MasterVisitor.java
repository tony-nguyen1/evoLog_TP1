package app;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import app.MyParser.CoupleNomData;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MasterVisitor extends ASTVisitor {

    private CompilationUnit compilationUnit;
    private int totalLines = 0;
    private int nbMethod = 0;

	private int nbMethodLine = 0;
    private int nbAttr = 0;
    private ArrayList<MyParser.CoupleNomData> list;
    private String nomClass;
    
    private List<TypeDeclaration> types = new ArrayList<TypeDeclaration>();

    public MasterVisitor(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
        list = new ArrayList<>();
    }

    @Override
    public boolean visit(TypeDeclaration node) {
    	nomClass = node.getName().getIdentifier();

        // Récupérer le nombre de lignes de la classe (ou interface)
        int startLine = compilationUnit.getLineNumber(node.getStartPosition());
        int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength());
        int classLineCount = endLine - startLine + 1;
        totalLines += classLineCount;
        /////////////////////////////
        
        if (!node.isInterface()) { types.add(node); }
        
        return super.visit(node);
    }

    @Override
    public boolean visit(MethodDeclaration node) {
    	this.nbMethod++;
    	
    	// Récupérer le nombre de lignes d'une méthode
		int startLine = compilationUnit.getLineNumber(node.getStartPosition());
		int endLine = compilationUnit.getLineNumber(node.getStartPosition() + node.getLength());
		int methodLineCount = endLine - startLine + 1;
		this.nbMethodLine += methodLineCount;
		
		// Récupérer le nom de la méthode
        String methodName = node.getName().getFullyQualifiedName();

        // Récupérer le nombre de paramètres
        int argumentCount = node.parameters().size();

        // Afficher le nom de la méthode et le nombre d'arguments
//        System.out.println("Méthode: " + methodName + " -> Nombre d'arguments: " + argumentCount);
        list.add(new CoupleNomData(this.nomClass+"::"+methodName, argumentCount));

		  
		return super.visit(node);
    }
    
    public boolean visit(VariableDeclarationFragment node) {
    	this.nbAttr++;
		return false;
	}
    
    private String packageName = "";

    @Override
    public boolean visit(PackageDeclaration node) {
        // Récupérer le nom du packages
        packageName = node.getName().getFullyQualifiedName();
        return false;
    }

    public String getPackageName() {
        return packageName;
    }
	
	public List<TypeDeclaration> getTypes() {
		return types;
	}
    

    public int getTotalLines() {
        return totalLines;
    }
    
    public int getNbMethodLine() {
		return nbMethodLine;
	}

	public int getNbAttr() {
		return nbAttr;
	}

	/**
	 * @return the list
	 */
	public ArrayList<MyParser.CoupleNomData> getList() {
		return list;
	}

    /**
	 * @return the nbMethod
	 */
	public int getNbMethod() {
		return nbMethod;
	}
}
