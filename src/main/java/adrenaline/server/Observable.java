package adrenaline.server;

import adrenaline.UpdateMessage;
import adrenaline.server.network.Client;

import java.rmi.RemoteException;
import java.util.ArrayList;

public abstract class Observable {
    protected ArrayList<Client> observers = new ArrayList<>();

    public boolean anyObserver(){
        return !observers.isEmpty();
    }
    public synchronized void attach(Client observer){ observers.add(observer); }
    public synchronized void detach(Client observer){ observers.remove(observer); }
    public synchronized void notifyObservers(UpdateMessage update){
            observers.forEach(x -> {
                try {
                    x.update(update);
                }catch (RemoteException e) { }
            });
    }


}
