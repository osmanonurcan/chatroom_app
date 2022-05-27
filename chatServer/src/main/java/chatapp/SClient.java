/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chatapp;

import static chatapp.Server.list_init;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.Thread.sleep;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author osman
 */
public class SClient {

    int[] received_content = new int[2];
    int id;
    public String name = "NoName";
    Socket soket;
    ObjectOutputStream sOutput;
    ObjectInputStream sInput;
    //clientten gelenleri dinleme threadi
    Listen listenThread;
  

    public SClient(Socket gelenSoket, int id) {
        this.soket = gelenSoket;
        this.id = id;
        try {
            this.sOutput = new ObjectOutputStream(this.soket.getOutputStream());
            this.sInput = new ObjectInputStream(this.soket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        //thread nesneleri
        this.listenThread = new Listen(this);
      

    }

    //client mesaj gönderme
    public void Send(Message message) {
        try {
            this.sOutput.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    //client dinleme threadi
    //her clientin ayrı bir dinleme thredi var
    class Listen extends Thread {

        SClient TheClient;

        //thread nesne alması için yapıcı metod
        Listen(SClient TheClient) {
            this.TheClient = TheClient;
        }

        @Override
        public void run() {
            //client bağlı olduğu sürece dönsün

            while (TheClient.soket.isConnected()) {
                try {
                    Message received;
                    //mesajı bekleyen kod satırı

                    received = (Message) TheClient.sInput.readObject();

                    //mesaj gelirse bu satıra geçer
                    //mesaj tipine göre işlemlere ayır
                    switch (received.type) {
                        case Name:
                            
                            Server.list_init = (Object[]) received.content;
                            Server.list_dlm_right.add((DefaultListModel) list_init[1]);
                            Server.dlm_left.addElement(list_init[0]);
                            list_init[0]= Server.dlm_left;
                            list_init[1]=Server.list_dlm_right;
                            Message msg = new Message(Message.Message_Type.Name);
                            msg.content = list_init;
                            Server.SendAll(msg);
                            break;

                        case Disconnect:
                            break;

                        case TextGroup:
                            Server.list_init = (Object[]) received.content;
                            int index = (int) Server.list_init[0];
                            String text = (String) Server.list_init[1];
                           
                            
                            Server.list_dlm_right.get(index).addElement(text);
                            Message textgroup = new Message(Message.Message_Type.TextGroup);
                            textgroup.content = Server.list_dlm_right;
                            Server.SendAll(textgroup);
                            break;
                        case TextPerson:
                            
                            break;
                        case CreateGroup:
                            String text_group = (String) received.content;
                            Server.dlm_left.addElement(text_group);
                            Server.list_dlm_right.add(new DefaultListModel());
                            list_init[0]= Server.dlm_left;
                            list_init[1]=Server.list_dlm_right;
                            Message msg_group = new Message(Message.Message_Type.CreateGroup);
                            msg_group.content = list_init;
                            Server.SendAll(msg_group);
                            break;

                        case Bitis:
                            break;

                    }

                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(SClient.class.getName()).log(Level.SEVERE, null, ex);
                    //client bağlantısı koparsa listeden sil
                    Server.Clients.remove(TheClient);

                }
                //client bağlantısı koparsa listeden sil

            }

        }
    }

    

}
