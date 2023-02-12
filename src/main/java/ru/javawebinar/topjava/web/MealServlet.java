package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;
import ru.javawebinar.topjava.repository.InMemoryUserMealRepositoryImpl;
import ru.javawebinar.topjava.util.UserMealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger LOG = getLogger(MealServlet.class);
    private InMemoryUserMealRepositoryImpl repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository =  new InMemoryUserMealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*        List<UserMealWithExcess> mealList = UserMealsUtil.filteredByStreams(UserMealsUtil.getTestData(), LocalTime.MIN,
                LocalTime.MAX, UserMealsUtil.CALORIES_PER_DAY);
        log.debug("Meal list was successfully created. Total "+mealList.size()+" position(s)");
        request.setAttribute("mealList", mealList);
        request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        log.debug("Redirect to Meal page");*/
        String action = request.getParameter("action");
        if(action==null){
            LOG.info("getAll");
            Collection<UserMealWithExcess> collection =  UserMealsUtil.getWithExceeded(repository.getAll(), UserMealsUtil.CALORIES_PER_DAY);
            request.setAttribute("mealList",collection);
            request.getRequestDispatcher("/mealList.jsp").forward(request, response);
        } else if (action.equals("delete")) {
            Integer id = getId(request);
            LOG.info("Delete {}");
            repository.delete(id);
            response.sendRedirect("userMeals");
        } else {
            final UserMeal meal = action.equals("create") ? new UserMeal(LocalDateTime.now(), "", 1000) :
                    repository.get(getId(request));
            request.setAttribute("meal",meal);
            request.getRequestDispatcher("mealEdit.jsp").forward(request,response);
        }
    }

    private Integer getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");
        UserMeal userMeal = new UserMeal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.valueOf(request.getParameter("calories")));
        LOG.info(userMeal.isNew() ? "Create {}":"Update {}",userMeal);
        repository.save(userMeal);
        response.sendRedirect("userMeals");
    }
}
