package app;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PackageCountVisitor extends ASTVisitor {
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
}
