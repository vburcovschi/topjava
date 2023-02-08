package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final MealRepository mealRepository = new MealRepository();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<MealTo> mealList = MealsUtil.filteredByStreams(mealRepository.getAll(), LocalTime.of(0, 0), LocalTime.of(23, 59), 2000);
        log.debug("Meal list was successfully created. Total "+mealList.size()+" position(s)");
        request.setAttribute("mealList", mealList);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
        log.debug("Redirect to Meal page");
    }
}
