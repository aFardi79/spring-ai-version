package ir.dotin.tempo;

public class Test {


    public static void main(String[] args) {
        HibernateSession session = HibernateManager.createSession();
        try{

        }finally {
            session.close();
        }
    }

}
