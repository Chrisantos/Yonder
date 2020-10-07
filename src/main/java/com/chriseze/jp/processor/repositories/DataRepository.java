package com.chriseze.jp.processor.repositories;

import com.chriseze.jp.processor.enums.ResponseEnum;
import com.chriseze.jp.processor.restartifacts.BaseResponse;
import com.chriseze.jp.processor.utils.ProxyUtil;
import com.chriseze.jp.processor.entities.Client;
import com.chriseze.jp.processor.entities.Project;
import com.chriseze.jp.processor.entities.Talent;
import com.chriseze.yonder.utils.enums.Industry;
import com.chriseze.yonder.utils.enums.ProjectStatus;
import com.chriseze.yonder.utils.repositories.AbstractBaseRepository;
import java.util.Collections;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class DataRepository extends AbstractBaseRepository {

    @PersistenceContext(name = "appService")
    private EntityManager entityManager;

    @Inject
    private ProxyUtil proxyUtil;

    protected static final Logger logger = LoggerFactory.getLogger(DataRepository.class);

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    public BaseResponse saveEntity(Object obj) {
        BaseResponse response = new BaseResponse(ResponseEnum.SUCCESS);
        try {
            proxyUtil.executeWithNewTransaction(() -> create(obj));
        } catch (Exception e) {
            response.assignResponseEnum(ResponseEnum.ERROR);
        }
        return response;
    }

    public Talent getTalentByEmailOrName(String param) {
        try {
            List<Talent> talent = entityManager.createNamedQuery(Talent.FIND_BY_EMAIL_OR_NAME, Talent.class)
                    .setParameter("param", param).getResultList();

            if (talent != null && !talent.isEmpty()) {
                return talent.get(0);
            }
        } catch (Exception e) {
            logger.error("Error occurred getting all talents by email or name", e);
        }
        return null;
    }

    public Client getClientByEmailOrName(String param) {
        try {
            List<Client> clients = entityManager.createNamedQuery(Client.FIND_BY_EMAIL_OR_NAME, Client.class)
                    .setParameter("param", param).getResultList();

            if (clients != null && !clients.isEmpty()) {
                return clients.get(0);
            }
        } catch (Exception e) {
            logger.error("Error occurred getting client by email or name", e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Talent> getTalents(Industry industry, String address) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Talent> criteriaQuery = criteriaBuilder.createQuery(Talent.class);
            Root<Talent> talentRoot = criteriaQuery.from(Talent.class);

            Predicate industryPredicate = criteriaBuilder.equal(talentRoot.get("industry"), industry);

            Predicate locationPredicate = criteriaBuilder.equal(talentRoot.get("address"), address);

            Predicate predicate = criteriaBuilder.and(locationPredicate, industryPredicate);

            criteriaQuery.where(predicate);

            return entityManager.createQuery(criteriaQuery).getResultList();
//            List<Talent> talents = entityManager.createQuery("select t from Talent t where t.industry = :industry and t.address like :address")
//                    .setParameter("industry", industry)
//                    .setParameter("address", "%" + address + "%")
//                    .getResultList();

//            return talents;

        } catch (Exception e) {
            logger.error("Error occurred retrieving talents", e);
        }

        return Collections.emptyList();
    }

    public List<Project> getAllProjects(ProjectStatus projectStatus) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
            Root<Project> projectRoot = criteriaQuery.from(Project.class);

            Predicate predicate = criteriaBuilder.equal(projectRoot.get("status"), projectStatus);
            criteriaQuery.where(predicate);

            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            logger.error("Error occurred retrieving projects", e);
        }

        return Collections.emptyList();
    }

    public List<Project> getAllProjectsByPoster(Client client) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Project> criteriaQuery = criteriaBuilder.createQuery(Project.class);
            Root<Project> projectRoot = criteriaQuery.from(Project.class);

            Predicate predicate = criteriaBuilder.equal(projectRoot.get("client"), client);
            criteriaQuery.where(predicate);

            return entityManager.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            logger.error("Error occurred retrieving projects", e);
        }

        return Collections.emptyList();
    }

    public List<Talent> getAllTalents() {
        try {
            return entityManager.createNamedQuery(Talent.FIND_ALL, Talent.class).getResultList();
        } catch (Exception e) {
            logger.error("Error occurred retrieving talents", e);
        }

        return null;
    }

}
