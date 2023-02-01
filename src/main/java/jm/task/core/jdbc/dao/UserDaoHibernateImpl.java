package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String SQL_TABLE = "CREATE TABLE USERS" +
            "(`ID` INT NOT NULL AUTO_INCREMENT," +
            "`NAME` VARCHAR (100) NOT NULL," +
            "`LASTNAME` VARCHAR (150) NOT NULL," +
            "`AGE` INT NOT NULL," +
            "PRIMARY KEY (`ID`)" +
            ")";
    private static final String dropTable = "DROP TABLE";
    private static final String cleanTAble = "DELETE FROM USERS\n"+
            "WHERE ID > 0";

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(SQL_TABLE).executeUpdate();
            transaction.commit();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            NativeQuery nativeQuery = session.createNativeQuery(dropTable);
            nativeQuery.executeUpdate();
            transaction.commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        try (Session session = Util.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            transaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.remove(session.get(User.class, id));
            transaction.commit();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        try (Session session = Util.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            criteria.from(User.class);
            users = session.createQuery(criteria).getResultList();
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        try(Session session = Util.getSessionFactory().openSession()){
            Transaction transaction = session.beginTransaction();
            NativeQuery nativeQuery = session.createSQLQuery(cleanTAble);
            nativeQuery.executeUpdate();
            transaction.commit();
        }
    }
}
