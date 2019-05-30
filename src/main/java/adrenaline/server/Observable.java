package adrenaline.server;

import adrenaline.UpdateMessage;
import adrenaline.server.network.Client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public abstract class Observable {
    protected ArrayList<Client> observers = new ArrayList<>();

    public void attach(Client observer){ observers.add(observer); }
    public void detach(Client observer){ observers.remove(observer); }
    public void notifyObservers(UpdateMessage update){
            observers.forEach(x -> {
                try {
                    x.update(update);
                }catch (RemoteException e) { }
            });
    }


}
