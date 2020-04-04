
package onlineShop.dao;

//.dao是用于存放和数据相关的代码
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import onlineShop.model.Authorities;
import onlineShop.model.Cart;
import onlineShop.model.Customer;
import onlineShop.model.User;

@Repository
//@Repository ==> create a bean, 功能类似于@component
public class CustomerDao {
	// 在ApplicationConfig里已经生成好，这里可以直接调用
	@Autowired
	private SessionFactory sessionFactory;

	public void addCustomer(Customer customer) {
		// 重置customer enbabled状态
		// 默认是false
		// enabled ： 判断用户是activity还得diactiviey
		customer.getUser().setEnabled(true);
		// 给用户权限
		Authorities authorities = new Authorities();
		authorities.setAuthorities("ROLE_USER");
		authorities.setEmailId(customer.getUser().getEmailId());
		// create cart
		Cart cart = new Cart();
		cart.setCustomer(customer);
		customer.setCart(cart);
		// create session
		Session session = null;

		try {
			// session是从sessionFactory内得到的
			session = sessionFactory.openSession();
			session.beginTransaction();
			session.save(authorities);
			// save里customer,有所customertable的数据都save了
			// 因为customer这里是cascade all
			session.save(customer);
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

//这里的user name是email，所以要到user的表格里去这个信息
	public Customer getCustomerByUserName(String userName) {
		User user = null;
		try (Session session = sessionFactory.openSession()) {
			session.beginTransaction();
			// 创建一个query对象
			// 把sql语句转化成对应的语句
			CriteriaBuilder builder = session.getCriteriaBuilder();
			// 访问user table
			CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
			// 查询语句
			// 输入的email等于user table中的email
			//root是指从根节点的node开始搜索数据
			Root<User> root = criteriaQuery.from(User.class);
			criteriaQuery.select(root).where(builder.equal(root.get("emailId"), userName));
			user = session.createQuery(criteriaQuery).getSingleResult();
			session.getTransaction().commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (user != null)
			return user.getCustomer();
		return null;
	}
}
