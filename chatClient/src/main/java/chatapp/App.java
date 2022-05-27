/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package chatapp;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author osman
 */
public class App extends javax.swing.JFrame {

    /**
     * Creates new form App
     */
    public Object[] list_init = new Object[2];
    public int id;
    public static App ThisApp;
    //public ArrayList<DefaultListModel> list_dlm_left = new ArrayList<>();
    public ArrayList<DefaultListModel> list_dlm_right = new ArrayList<>();
    public DefaultListModel dlm_left = new DefaultListModel();
    public JList list_left = new JList();
    public JList list_right = new JList();
    public JTextArea txt_msg = new JTextArea();
    public JButton btn_send = new JButton();
    public JTextField txt_name = new JTextField();
    public JButton btn_connect = new JButton("Connect");
    public JButton btn_create_group = new JButton("Create Group");
    public JTextField txt_group = new JTextField();
    public JScrollPane sp_right = new JScrollPane(list_right, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    public JScrollPane sp_left = new JScrollPane(list_left, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    public ImageIcon icon_send = new ImageIcon(new ImageIcon("send.png").getImage().getScaledInstance(15, 15, Image.SCALE_DEFAULT));

    public App() {
        initComponents();

        ThisApp = this;

        txt_name.setBounds(10, 10, 80, 30);
        ThisApp.add(txt_name);

        btn_connect.setBounds(100, 10, 80, 30);
        btn_connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Client.Start("54.172.144.52", 2000);
                Client.Start("localhost", 2000);
                //başlangıç durumları

                btn_connect.setEnabled(false);
                txt_name.setEnabled(false);

            }

        });
        ThisApp.add(btn_connect);

        txt_group.setBounds(280, 10, 80, 30);
        ThisApp.add(txt_group);

        btn_create_group.setBounds(370, 10, 120, 30);
        btn_create_group.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                String text = "Group: " + txt_group.getText();
                Message msg = new Message(Message.Message_Type.CreateGroup);
                msg.content = text;
                Client.Send(msg);
                txt_group.setText("");
            }
        });
        ThisApp.add(btn_create_group);

        list_left.setModel(dlm_left);
        list_left.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list_left.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    //System.out.println(list_dlm_right.size());
                    //System.out.println(list_left.getSelectedIndex());
                    list_right.setModel(list_dlm_right.get(list_left.getSelectedIndex()));
                }
            }
        });

        sp_left.setBounds(10, 50, 170, 300);
        ThisApp.add(sp_left);

        sp_right.setBounds(190, 50, 300, 300);
        ThisApp.add(sp_right);

        txt_msg.setBounds(10, 360, 445, 30);
        ThisApp.add(txt_msg);

        btn_send.setBounds(460, 360, 30, 30);
        btn_send.setIcon(icon_send);
        btn_send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selected = list_left.getSelectedValue().toString();
                if(selected.contains("Group: ")){
                 
                    Message msg = new Message(Message.Message_Type.TextGroup);
                    list_init[0] = list_left.getSelectedIndex();
                    list_init[1] = txt_msg.getText();
                    msg.content = list_init;
                    Client.Send(msg);
                }
                else{
                    list_dlm_right.get(list_left.getSelectedIndex()).addElement(txt_msg.getText());
                    DefaultListModel model = list_dlm_right.get(list_left.getSelectedIndex());
                    
                }

            }
        });
        ThisApp.add(btn_send);

        ThisApp.setBounds(0, 0, 515, 440);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(App.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new App().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
