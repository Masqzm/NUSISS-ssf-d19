package ssf.day19.models;

import java.io.StringReader;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.json.Json;
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

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @FutureOrPresent(message = "Due date must be in the present day or future!")
    private Date dueDate; // to be stored as epoch ms in Redis

    private String priority; // low/medium/high
    private String status; // pending/started/in_progress/completed
    private Date createdAt; // Date of creation (to be stored as epoch ms in Redis)
    private Date updatedAt; // Updated Date each time record is updated (to be stored as epoch ms in Redis)

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
        Date dueDate = new Date(j.getJsonNumber("due_date").longValue());
        Date createdDate = new Date(j.getJsonNumber("created_at").longValue());
        Date updatedDate = new Date(j.getJsonNumber("updated_at").longValue());

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

    public String toJson() {
        JsonObject job = Json.createObjectBuilder()
                        .add("id", this.id)
                        .add("name", this.name)
                        .add("description", this.description)
                        .add("due_date", this.dueDate.getTime())
                        .add("priority_level", this.priority)
                        .add("status", this.status)
                        .add("created_at", this.createdAt.getTime())
                        .add("updated_at", this.updatedAt.getTime())
                        .build();

        return job.toString();
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
