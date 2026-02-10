package ui;

import br.edu.ifpe.ui.config.ThemeConfig;
import br.edu.ifpe.ui.frames.MainWindow;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.finder.JFileChooserFinder;
import org.assertj.swing.finder.JOptionPaneFinder;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JButtonFixture;
import org.assertj.swing.fixture.JFileChooserFixture;
import org.assertj.swing.fixture.JOptionPaneFixture;
import org.assertj.swing.testing.AssertJSwingTestCaseTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FormTest extends AssertJSwingTestCaseTemplate {

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
    public void emptyForm() {
        JButtonFixture forwardBtn = window.button("forwardBtn");

        while (forwardBtn.isEnabled()) {
            forwardBtn.click();
            robot().waitForIdle();
        }

        JButtonFixture genBtn = window.button("genBtn");
        genBtn.click();

        JFileChooserFixture selector = JFileChooserFinder.findFileChooser()
                .withTimeout(5000)
                .using(robot());

        selector.approve();

        JOptionPaneFixture dialog = JOptionPaneFinder.findOptionPane()
                .withTimeout(10000)
                .using(robot());

        dialog.okButton().click();
    }

    @AfterEach
    public void tearDown() {
        this.cleanUp();
    }
}