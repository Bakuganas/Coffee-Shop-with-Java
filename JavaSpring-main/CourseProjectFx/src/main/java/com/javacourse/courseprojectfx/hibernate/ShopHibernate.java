package com.javacourse.courseprojectfx.hibernate;

import com.javacourse.courseprojectfx.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ShopHibernate extends GenericHibernate {
    public ShopHibernate(EntityManagerFactory entityManagerFactory) {
        super(entityManagerFactory);
    }

    public void deleteWarehouse(int id) {
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            //Get the cart that we plan to delete
            var warehouse = entityManager.find(Warehouse.class, id);

            List<Product> products = warehouse.getProductList();
            for (Product p : products) {
                Product product = entityManager.find(Product.class, p.getId());
                product.setWarehouse(null);
                entityManager.merge(product);
            }
            List<Manager> managers = warehouse.getEmployees();
            for (Manager m : managers) {
                Manager manager = entityManager.find(Manager.class, m.getId());
                manager.setWarehouse(null);
                entityManager.merge(manager);
            }
            warehouse.getProductList().clear();
            warehouse.getEmployees().clear();
            entityManager.remove(warehouse);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }


    //This is a custom method for creating cart. It is more complicated, because cart must jave  abuyer assigned,
    //products must be assigned to cart
    public void createCart(List<Product> products, User user) {
        //All must happen within the same transaction
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            //First get the customer
            Customer buyer = entityManager.find(Customer.class, user.getId());
            //Then we go through the objects that user wants to buy
            Cart cart = new Cart(new ArrayList<>(), buyer);
            for (Product p : products) {
                Product product = entityManager.find(Product.class, p.getId());
                //Assign cart to product and product to cart. So that data would be properly stored in db
                product.setCart(cart);
                cart.getProductList().add(product);
            }
            buyer.getMyCarts().add(cart);
            //Update buyer, we have cascade all, so this new cart will be inserted and ids assigned
            entityManager.merge(buyer);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    //When deleting, generic delete method will word in situations when the record you are trying to delete is not linked to other records
    //In this case cart is linked to user and products (and even manager)
    //You must first unlink and then delete
    public void deleteCart(int id) {
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            //Get the cart that we plan to delete
            var cart = entityManager.find(Cart.class, id);

            if (cart.getCustomer() != null) {
                Customer customer = entityManager.find(Customer.class, cart.getCustomer().getId());
                //remove this cart from customer cart list
                customer.getMyCarts().remove(cart);
                //update these changes within this transaction
                entityManager.merge(customer);
            }
            if (cart.getManager() != null) {
                Manager manager = entityManager.find(Manager.class, cart.getManager().getId());
                manager.getMyManagedCarts().remove(cart);
                entityManager.merge(manager);
            }
            List<Product> products = cart.getProductList();
            for (Product p : products) {
                Product product = entityManager.find(Product.class, p.getId());
                product.setCart(null);
            }
            cart.getProductList().clear();
            entityManager.remove(cart);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }


    public <T> T getUserByCredentials(String login, String password) {
        EntityManager em = getEntityManager();
        try {
            Manager manager = getManagerByCredentials(login, password);
            return manager == null ? (T) getCustomerByCredentials(login, password) : (T) manager;
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }
    public Manager getManagerByCredentials(String login, String password) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Manager> query = cb.createQuery(Manager.class);
            Root<Manager> root = query.from(Manager.class);
            query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), password)));
            Query q;

            q = em.createQuery(query);
            return (Manager) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public Customer getCustomerByCredentials(String login, String password) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Customer> query = cb.createQuery(Customer.class);
            Root<Customer> root = query.from(Customer.class);
            query.select(root).where(cb.and(cb.like(root.get("login"), login), cb.like(root.get("password"), password)));
            Query q;

            q = em.createQuery(query);
            return (Customer) q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void deleteComment(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            var comment = em.find(Comment.class, id);
            Product product = em.find(Product.class, comment.getProduct().getId());
            product.getComments().remove(comment);
            em.merge(product);

            User user = getUserByCredentials(comment.getOwner().getLogin(), comment.getOwner().getPassword());
            user.getComments().remove(comment);
            em.merge(user);

            comment.getReplies().clear();
            em.remove(comment);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> loadAvailableProducts(){
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> root = query.from(Product.class);
            query.select(root).where(cb.isNull(root.get("cart")));
            Query q;

            q = em.createQuery(query);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void addWarehouseData(List<Product> products, List<Manager> managers, String address, City city) {
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            Warehouse warehouse = new Warehouse(address,new ArrayList<>(),new ArrayList<>(),city);
            for (Product p : products) {
                Product product = entityManager.find(Product.class, p.getId());
                product.setWarehouse(warehouse);
                warehouse.getProductList().add(product);
            }
            for (Manager m : managers) {
                Manager manager = entityManager.find(Manager.class, m.getId());
                manager.setWarehouse(warehouse);
                warehouse.getEmployees().add(manager);
            }
            entityManager.merge(warehouse);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public void deleteWarehouseData(int id) {
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();
            var warehouse = entityManager.find(Warehouse.class, id);
            List<Product> products = warehouse.getProductList();
            for (Product p : products) {
                Product product = entityManager.find(Product.class, p.getId());
                product.setWarehouse(null);
                entityManager.merge(product);
            }
            List<Manager> managers = warehouse.getEmployees();
            for (Manager m : managers) {
                Manager manager = entityManager.find(Manager.class, m.getId());
                manager.setWarehouse(null);
                entityManager.merge(manager);
            }
            warehouse.getProductList().clear();
            warehouse.getEmployees().clear();
            entityManager.remove(warehouse);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
    public List<Product> loadAvailableProductsForWarehouse(){
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Product> query = cb.createQuery(Product.class);
            Root<Product> root = query.from(Product.class);
            query.select(root).where(cb.isNull(root.get("warehouse")));
            Query q;

            q = em.createQuery(query);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public List<Manager> loadAvailableManagersForWarehouse(){
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Manager> query = cb.createQuery(Manager.class);
            Root<Manager> root = query.from(Manager.class);
            query.select(root).where(cb.isNull(root.get("warehouse")));
            Query q;

            q = em.createQuery(query);
            return q.getResultList();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) em.close();
        }
    }

    public void createWarehouseInDB(List<Product> products, List<Manager> managers, String address, City city) {
        //All must happen within the same transaction
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            //Then we go through the objects that user wants to buy
            Warehouse warehouse = new Warehouse(address,new ArrayList<>(),new ArrayList<>(),city);

            for (Product p : products) {
                Product product = entityManager.find(Product.class, p.getId());
                //Assign cart to product and product to cart. So that data would be properly stored in db
                product.setWarehouse(warehouse);
                warehouse.getProductList().add(product);
            }
            for (Manager m : managers) {
                Manager manager = entityManager.find(Manager.class, m.getId());
                //Assign cart to product and product to cart. So that data would be properly stored in db
                manager.setWarehouse(warehouse);
                warehouse.getEmployees().add(manager);
            }
            //Update buyer, we have cascade all, so this new cart will be inserted and ids assigned
            entityManager.merge(warehouse);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }
    public void updateWarehouse(int id, List<Product> productsListView, List<Manager> managersListView){
        try {
            entityManager = getEntityManager();
            entityManager.getTransaction().begin();

            //Then we go through the objects that user wants to buy
            Warehouse warehouse = entityManager.find(Warehouse.class,id);

            //Istrina warehouse listus
            List<Product>products=warehouse.getProductList();
            for (Product p : products) {
                Product product = entityManager.find(Product.class, p.getId());
                product.setWarehouse(null);
                entityManager.merge(product);
                //Assign cart to product and product to cart. So that data would be properly stored in db;
            }

            warehouse.getProductList().clear();
            List<Manager>managers=warehouse.getEmployees();
            for (Manager m : managers) {
                Manager manager = entityManager.find(Manager.class, m.getId());
                //Assign cart to product and product to cart. So that data would be properly stored in db
                manager.setWarehouse(null);
                entityManager.merge(manager);
            }
            warehouse.getEmployees().clear();
            //Per naujo prideda warehouse
            for (Product p : productsListView) {
                Product product = entityManager.find(Product.class, p.getId());
                //Assign cart to product and product to cart. So that data would be properly stored in db
                product.setWarehouse(warehouse);
                entityManager.merge(product);
                warehouse.getProductList().add(product);

            }
            for (Manager m : managersListView) {
                Manager manager = entityManager.find(Manager.class, m.getId());
                //Assign cart to product and product to cart. So that data would be properly stored in db
                manager.setWarehouse(warehouse);
                entityManager.merge(manager);
                warehouse.getEmployees().add(manager);
            }
            //Update buyer, we have cascade all, so this new cart will be inserted and ids assigned
            entityManager.merge(warehouse);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (entityManager != null) entityManager.close();
        }
    }

    public List<Cart> filterOrders(LocalDate dateFrom, Customer customer, String id) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Cart> query = cb.createQuery(Cart.class);
            Root<Cart> root = query.from(Cart.class);
            Predicate predicate = cb.conjunction(); // Initialize predicate as a conjunction (AND)
            // Add conditions based on the provided parameters
            if (dateFrom != null) {
                predicate = cb.and(predicate, cb.greaterThan(root.get("dateCreated"), dateFrom));
            }
            if (customer != null) {
                predicate = cb.and(predicate, cb.equal(root.get("customer"), customer));
            }
            if (id != null) {
                predicate = cb.and(predicate, cb.equal(root.get("id"), id));
            }

            query.select(root).where(predicate); // Apply the predicate to the query

            jakarta.persistence.Query q = em.createQuery(query);
            return q.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList(); // Return an empty list if no results are found
        } finally {
            if (em != null) em.close();
        }
    }
}