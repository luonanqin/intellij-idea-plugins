package luonq;

import com.intellij.ide.SelectInTarget;
import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.AbstractProjectViewPane;
import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Luonanqin on 11/13/14.
 */
public class ScrollFromSource extends AnAction {

	private static final Logger LOG = Logger.getInstance(ScrollFromSource.class);

	private Project myProject;

	public void actionPerformed(AnActionEvent e) {
		myProject = e.getProject();

		final FileEditorManager fileEditorManager = FileEditorManager.getInstance(myProject);
		final Editor selectedTextEditor = fileEditorManager.getSelectedTextEditor();
		if (selectedTextEditor != null) {
			selectElementAtCaret(selectedTextEditor);
			return;
		} else {
			LOG.error("selectedTextEditor is null!");
		}
	}

	private void selectElementAtCaret(@NotNull Editor editor) {
		final PsiFile file = PsiDocumentManager.getInstance(myProject).getPsiFile(editor.getDocument());
		if (file == null) {
			LOG.error("psiFile is null");
			return;
		}

		scrollFromFile(file, editor);
	}

	private void scrollFromFile(@NotNull PsiFile file, @Nullable Editor editor) {
		final MySelectInContext selectInContext = new MySelectInContext(file, editor, myProject);

		ProjectViewImpl projectView = (ProjectViewImpl) ProjectView.getInstance(myProject);
		AbstractProjectViewPane currentProjectViewPane = projectView.getCurrentProjectViewPane();
		SelectInTarget target = currentProjectViewPane.createSelectInTarget();
		if (target != null && target.canSelect(selectInContext)) {
			target.selectIn(selectInContext, false);
		}
	}
}
