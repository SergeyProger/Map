/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pgot;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author Сергей
 */
 public class MainPanel extends JPanel {
    private final PaintPanel paintPanel;
    private final JSlider smoothSlider;
 
    
    public MainPanel() {
        setLayout(new BorderLayout());
        paintPanel = new PaintPanel();
      
        add(paintPanel, BorderLayout.CENTER);
        final JPanel configPanel = new JPanel();
        configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.LINE_AXIS));
        configPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        configPanel.add(new JLabel("Шаг сетки"));
        
        smoothSlider = new JSlider();
        smoothSlider.setMinimum(100);
        smoothSlider.setMaximum(700);
        smoothSlider.setValue(300);
        smoothSlider.addChangeListener(smoothListener);
        configPanel.add(smoothSlider);
        
        final JButton generateButton = new JButton("Генерация");
        generateButton.addActionListener(generateListener);
        configPanel.add(generateButton);
        
        add(configPanel, BorderLayout.PAGE_START);
    }
    
    private final ChangeListener smoothListener = new ChangeListener() {

        @Override
        public void stateChanged(ChangeEvent e) {
              paintPanel.setSmoothIterations(smoothSlider.getValue());
        }
    };
    
    private final ActionListener generateListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
              paintPanel.generateNew();
        }
    };
}
