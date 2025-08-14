package ir.dotin.tempo;

public class Test {


    public static void main(String[] args) {
        HibernateSession session = HibernateManager.createSession();
        doWork(session);
    }


    public static void  doWork(HibernateSession session){
        session.CreateQuery();
        session.close();
    }

}
