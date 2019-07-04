package adrenaline.server;

import adrenaline.UpdateMessage;
import adrenaline.server.network.Client;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * Observer pattern's abstract class
 *
 */
public abstract class Observable {
    protected ArrayList<Client> observers = new ArrayList<>();

    /**
     *
     * Return if there are observers
     * @return false for observer empty
     */
    public boolean anyObserver(){
        return !observers.isEmpty();
    }

    /**
     *
     * For add observers
     * @param observer The observer which have to be attach
     */
    public synchronized void attach(Client observer){ observers.add(observer); }

    /**
     * For detach observer
     * @param observer The observer which have to be detach
     */
    public synchronized void detach(Client observer){ observers.remove(observer); }

    /**
     *
     * For notyfy all observers
     * @param update The update message
     */
    public synchronized void notifyObservers(UpdateMessage update){
            observers.forEach(x -> {
                try {
                    x.update(update);
                }catch (RemoteException e) { }
            });
    }


}
