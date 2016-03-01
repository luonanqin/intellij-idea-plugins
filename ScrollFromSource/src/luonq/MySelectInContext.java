package luonq;

import com.intellij.ide.FileEditorProvider;
import com.intellij.ide.SelectInContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Luonanqin on 3/1/16.
 */
public class MySelectInContext implements SelectInContext {
	@NotNull
	private final PsiFile myPsiFile;
	@Nullable
	private final Editor myEditor;
	@Nullable
	private final Project myProject;

	public MySelectInContext(@NotNull PsiFile psiFile, @Nullable Editor editor, Project myProject) {
		myPsiFile = psiFile;
		myEditor = editor;
		this.myProject = myProject;
	}

	@Override
	@NotNull
	public Project getProject() {
		return myProject;
	}

	@NotNull
	private PsiFile getPsiFile() {
		return myPsiFile;
	}

	@Override
	@NotNull
	public FileEditorProvider getFileEditorProvider() {
		return null;
	}

	@NotNull
	private PsiElement getPsiElement() {
		PsiElement e = null;
		if (myEditor != null) {
			final int offset = myEditor.getCaretModel().getOffset();
			PsiDocumentManager.getInstance(myProject).commitAllDocuments();
			e = getPsiFile().findElementAt(offset);
		}
		if (e == null) {
			e = getPsiFile();
		}
		return e;
	}

	@Override
	@NotNull
	public VirtualFile getVirtualFile() {
		return getPsiFile().getVirtualFile();
	}

	@Override
	public Object getSelectorInFile() {
		return getPsiElement();
	}
}

