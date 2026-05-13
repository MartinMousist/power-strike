package com.powerstrike.service;

import com.powerstrike.model.Activity;
import com.powerstrike.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public List<Activity> getAllActivities() {
        return activityRepository.findAll();
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Actividad no encontrada"));
    }

    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity updateActivity(Long id, Activity updated) {
        Activity activity = getActivityById(id);
        activity.setName(updated.getName());
        activity.setDay(updated.getDay());
        activity.setSchedule(updated.getSchedule());
        activity.setMonthlyCost(updated.getMonthlyCost());
        activity.setActive(updated.isActive());
        return activityRepository.save(activity);
    }

    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }
}
