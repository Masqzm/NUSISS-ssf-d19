package ssf.day19.models;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;

public class Task {
    @Size(max = 50)
    private String id; // to be generated using UUID

    @Size(min = 10, max = 50, message = "Name must be at 10 to 50 characters long!")
    private String name;

    @Size(max = 255, message = "Description must not be more than 255 characters!")
    private String description;

    @FutureOrPresent(message = "Due date must be in the present day or future!")
    private Date dueDate; // to be stored as epoch ms in Redis

    private String priority; // Low/Medium/High
    private String status; // Pending/Started/Progress/Completed
    private Date createdAt; // Date of creation (to be stored as epoch ms in Redis)
    private Date updatedAt; // Updated Date each time record is updated (to be stored as epoch ms in Redis)

    public static final String DATE_FORMAT = "EEE, MM/dd/yyyy";

    public Task() {
    }

    public Task(String id, String name, String description, Date dueDate, String priority, String status,
            Date createdAt, Date updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Task jsonToTask(String json) {
        if (json == null)
            return null;

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        // Convert String epoch ms -> Long -> Date
        Date dueDate = new Date(Long.parseLong(j.getString("due_date")));
        Date createdDate = new Date(Long.parseLong(j.getString("created_at")));
        Date updatedDate = new Date(Long.parseLong(j.getString("updated_at")));

        Task task = new Task(j.getString("id"),
                j.getString("name"),
                j.getString("description"),
                dueDate,
                j.getString("priority_level"),
                j.getString("status"),
                createdDate,
                updatedDate);

        return task;
    }

    @Override
    public String toString() {
        return "Task [id=" + id + ", name=" + name + ", description=" + description + ", dueDate=" + dueDate
                + ", priority=" + priority + ", status=" + status + ", createdAt=" + createdAt + ", updatedAt="
                + updatedAt + "]";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
