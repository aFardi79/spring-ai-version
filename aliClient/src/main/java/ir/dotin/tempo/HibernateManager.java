package ir.dotin.tempo;

public class HibernateManager {



    public static HibernateSession createSession() {
        return new HibernateSession();
    }
}
