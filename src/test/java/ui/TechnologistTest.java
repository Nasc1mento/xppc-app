package ui;

import br.edu.ifpe.ui.config.ThemeConfig;
import br.edu.ifpe.ui.frames.MainWindow;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.testing.AssertJSwingTestCaseTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TechnologistTest extends AssertJSwingTestCaseTemplate {

    private FrameFixture window;

    @BeforeAll
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeEach
    public void setup() {
        this.setUpRobot();
        ThemeConfig.setup();
        MainWindow frame = GuiActionRunner.execute(MainWindow::new);
        window = new FrameFixture(robot(), frame);
        window.show();
    }

    @Test
    public void cover() {
        window.button();
    }

    @AfterEach
    public void tearDown() {
        this.cleanUp();
    }
}