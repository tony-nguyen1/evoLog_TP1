package app;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MethodCountVisitor extends ASTVisitor {
    private int nbMethod = 0;
    
    @Override
    public boolean visit(MethodDeclaration node) {
        nbMethod ++;
        return super.visit(node);
  }

    public int getNbMethods() {
        return nbMethod;
    }
}
