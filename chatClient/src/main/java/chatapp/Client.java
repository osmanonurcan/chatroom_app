/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;


import static chatapp.Client.sInput;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

/**
 *
 * @author osman
 */
// serverdan gelecek mesajları dinleyen thread
class Listen extends Thread {

    @Override
    public void run() {
        //soket bağlı olduğu sürece dön
        boolean first = true;
        while (Client.socket.isConnected()) {
            try {
                //mesaj gelmesini bloking olarak dinyelen komut
                Message received = (Message) (sInput.readObject());
                //mesaj gelirse bu satıra geçer
                //mesaj tipine göre yapılacak işlemi ayır.
                switch (received.type) {
                    case Name:
                        App.ThisApp.list_init = (Object[])received.content;
                        
                        App.ThisApp.dlm_left = (DefaultListModel) App.ThisApp.list_init[0];
                        if(first){
                            App.ThisApp.id = App.ThisApp.dlm_left.size()-1;
                            first = false;
                        }
                        App.ThisApp.list_dlm_right = (ArrayList<DefaultListModel>) App.ThisApp.list_init[1];
                        
                        App.ThisApp.list_left.setModel(App.ThisApp.dlm_left);
                        
                        break;
                    case TextPerson:
                        break;
                    case CreateGroup:
                        App.ThisApp.list_init = (Object[])received.content;
                        App.ThisApp.dlm_left = (DefaultListModel) App.ThisApp.list_init[0];
                        App.ThisApp.list_dlm_right = (ArrayList<DefaultListModel>) App.ThisApp.list_init[1];
                        App.ThisApp.list_left.setModel(App.ThisApp.dlm_left);
                        break;
                    case Disconnect:
                        break;
                    case TextGroup:
                        App.ThisApp.list_dlm_right = (ArrayList<DefaultListModel>) received.content;
                        break;
                     
                    case Bitis:
                        break;

                }

            } catch (IOException | ClassNotFoundException ex) {

                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                //Client.Stop();
                break;
            }
            //Client.Stop();

        }

    }
}

public class Client {

    //her clientın bir soketi olmalı
    public static Socket socket;

    //verileri almak için gerekli nesne
    public static ObjectInputStream sInput;
    //verileri göndermek için gerekli nesne
    public static ObjectOutputStream sOutput;
    //serverı dinleme thredi 
    public static Listen listenMe;
    
    

    public static void Start(String ip, int port) {
        try {
            // Client Soket nesnesi
            Client.socket = new Socket(ip, port);
            Client.Display("Servera bağlandı");
            // input stream
            Client.sInput = new ObjectInputStream(Client.socket.getInputStream());
            // output stream
            Client.sOutput = new ObjectOutputStream(Client.socket.getOutputStream());
            Client.listenMe = new Listen();
            Client.listenMe.start();

            //ilk mesaj olarak isim gönderiyorum
            Message msg = new Message(Message.Message_Type.Name);
            App.ThisApp.list_dlm_right.add(new DefaultListModel());
            
            App.ThisApp.list_init[0] = App.ThisApp.txt_name.getText();
            App.ThisApp.list_init[1] = App.ThisApp.list_dlm_right.get(0);
            
            msg.content = App.ThisApp.list_init;
            Client.Send(msg);
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //client durdurma fonksiyonu
    public static void Stop() {
        try {
            if (Client.socket != null) {
                Client.listenMe.stop();
                Client.socket.close();
                Client.sOutput.flush();
                Client.sOutput.close();

                Client.sInput.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void Display(String msg) {

        System.out.println(msg);

    }

    //mesaj gönderme fonksiyonu
    public static void Send(Message msg) {
        try {
            Client.sOutput.writeObject(msg);
            Client.sOutput.reset();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
