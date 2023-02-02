package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import net.bytebuddy.asm.Advice;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final String SQL_TABLE = "CREATE TABLE IF NOT EXISTS `instructor`.`usersTable` " +
            " (`id` INT NOT NULL AUTO_INCREMENT," +
            "`name` VARCHAR(45) NOT NULL," +
            "`lastName` VARCHAR(45) NOT NULL," +
            "`age` INT NOT NULL," +
            " PRIMARY KEY (`id`)) " +
            "ENGINE = InnoDB " +
            "DEFAULT CHARACTER SET = utf8";
    private static final String dropTable = "DROP TABLE usersTable";
    private static final String cleanTAble = "DELETE FROM usersTable\n" +
            "WHERE ID > 0";

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {
        execute(SQL_TABLE);
    }

    @Override
    public void dropUsersTable() {
       execute(dropTable);
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        Session session = Util.getSessionFactory().openSession();
        try{
            transaction = session.beginTransaction();
            session.save(new User(name,lastName,age));
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
                e.printStackTrace();
            }
        }  finally {
            try{
                session.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        Session session = Util.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.remove(session.get(User.class, id));
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }finally {
            try{
                session.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = null;
        Session session = Util.getSessionFactory().openSession();
        try {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            criteria.from(User.class);
            users = session.createQuery(criteria).getResultList();
        } finally {
            try{
                session.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        execute(cleanTAble);
    }
    public void execute (String sqlCommand ) {
        Transaction transaction = null;
        Session session = Util.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.createSQLQuery(sqlCommand).executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (null != transaction) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            try{
                session.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
