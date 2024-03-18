package dev.imake.idef1loader;

import com.intellij.ide.PowerSaveMode;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class RacingProgressBarJava extends RacingProgressBar {
    public static @NotNull ComponentUI createUI(JComponent c) {
        boolean powerMode = PowerSaveMode.isEnabled();
        if (powerMode) {
            return new BasicProgressBarUI();
        } else {
            return new RacingProgressBar();
        }
    }
}
