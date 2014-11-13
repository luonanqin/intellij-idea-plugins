package luonq;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.ide.projectView.impl.ProjectViewImpl;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Luonanqin on 11/13/14.
 */
public class ScrollFromSource extends AnAction {

	private static final Logger LOG = Logger.getInstance(ScrollFromSource.class);

	public void actionPerformed(AnActionEvent e) {
		try {
			Project project = e.getProject();
			ProjectViewImpl projectView = (ProjectViewImpl) ProjectView.getInstance(project);

			Class<ProjectViewImpl> clazz = ProjectViewImpl.class;

			Field[] fields = clazz.getDeclaredFields();
			Field myAutoScrollFromSourceHandlerField = null;
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				// can't use field's name to find the variable of MyAutoScrollFromSourceHandler
				if ("MyAutoScrollFromSourceHandler".equals(field.getType().getSimpleName())) {
					myAutoScrollFromSourceHandlerField = field;
				}
			}

			myAutoScrollFromSourceHandlerField.setAccessible(true);
			Object handler = myAutoScrollFromSourceHandlerField.get(projectView);

			Class<?>[] clazzes = clazz.getDeclaredClasses();
			for (int i = 0; i < clazzes.length; i++) {
				Class<?> clazze = clazzes[i];
				String simpleName = clazze.getSimpleName();
				if ("MyAutoScrollFromSourceHandler".equals(simpleName)) {
					Method fromSource = clazze.getMethod("scrollFromSource");
					fromSource.setAccessible(true);
					fromSource.invoke(handler);
					return;
				}
			}
		} catch (Exception ex) {
			LOG.error("ScrollFromSource execute ERROR!", ex);
		}
	}
}
