package Editors;

/**
 *
 * @author Julian Craske
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import Vessel.Ship.Mount;
import Vessel.Ship.ShipModel;

public class ModelEditor extends JPanel {
    private Vector<ShipModel> models;
    private final ModelView view = new ModelView();
    private JList mountList;
    private JList modelList;

    private ShipModel selectedModel = null;

    public ModelEditor() {
        setLayout(new BorderLayout());
        Container contents = new JPanel();
        add(contents, BorderLayout.CENTER);

        Container topPanel = new JPanel();
        add(topPanel, BorderLayout.NORTH);

        JButton newModel = new JButton("New Model");
        newModel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String imagePath = JOptionPane.showInputDialog("Enter the image path.");
                ShipModel model = new ShipModel(imagePath);
                ShipModel.addModel(model);
                selectModel(model);
                refresh();
            }
        });
        topPanel.add(newModel);

        JButton saveModels = new JButton("Save Models");
        saveModels.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ShipModel.saveModels();
            }
        });
        topPanel.add(saveModels);

        view.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {
                Mount m = selectedModel.getMount(mountList.getSelectedIndex());
                m.setPosition(e.getX() - view.getWidth()/2, e.getY() - view.getHeight()/2);
                repaint();
            }

            public void mousePressed(MouseEvent e) { }
            public void mouseReleased(MouseEvent e) { }
            public void mouseEntered(MouseEvent e) { }
            public void mouseExited(MouseEvent e) { }
        });
        contents.add(view);

        mountList = new JList(ShipModel.getMountNames());
        add(mountList, BorderLayout.EAST);
        mountList.setSelectedIndex(0);
        mountList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                repaint();
            }
        });

        modelList = new JList();
        modelList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                selectModel(models.get(modelList.getSelectedIndex()));
            }
        });
        add(modelList, BorderLayout.WEST);
        refresh();
    }

    private void refresh() {
        models = ShipModel.getModels();
        modelList.setListData(models);
        repaint();
    }

    public void selectModel(ShipModel model) {
        selectedModel = model;
        view.setModel(model);
        repaint();
    }

    public static void main(String[] args) {
        JFrame window = new JFrame("Model Editor");
        window.add(new ModelEditor());
        window.setVisible(true);
        window.setSize(600, 400);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public class ModelView extends JPanel {
        public ModelView() {

        }

        public void paint(Graphics g) {
            if(selectedModel != null) {
                g.drawImage(selectedModel.getImage(), 0, 0, this);
                for(Mount m : selectedModel.getMounts()) {
                    if(m == selectedModel.getMount(mountList.getSelectedIndex())) {
                        g.setColor(Color.red);
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    int x = (int)m.getX() + getWidth()/2;
                    int y = (int)m.getY() + getHeight()/2;
                    g.drawLine(x-3, y, x+3, y);
                    g.drawLine(x, y-3, x, y+3);
                }
            } else {
                g.setColor(Color.black);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        public void setModel(ShipModel model) {
            setSize(model.getImage().getWidth(), model.getImage().getHeight());
        }
    }
}
