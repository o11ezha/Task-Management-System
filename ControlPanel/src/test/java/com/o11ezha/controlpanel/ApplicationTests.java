package com.o11ezha.controlpanel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.o11ezha.controlpanel.DTO.model.UserModel;
import com.o11ezha.controlpanel.DTO.request.*;
import com.o11ezha.controlpanel.DTO.response.CommentCreationResponse;
import com.o11ezha.controlpanel.DTO.response.JWTResponse;
import com.o11ezha.controlpanel.DTO.response.TaskCreationResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterUser() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("user@example.com", "Full Name", "passworD123@");
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void testAuthenticateUser() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "passworD123@");

        mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCreateTasks() throws Exception {
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("TaskName5", "taskTheme5", "taskDesc5", "MEDIUM");

        mockMvc.perform(post("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + obtainJwtToken())
                        .content(objectMapper.writeValueAsString(taskCreationRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testGetTasks() throws Exception {

        mockMvc.perform(get("/v1/tasks")
                        .header("Authorization", "Bearer " + obtainJwtToken()))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.tasks").exists());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    public void testGetAuthorTasks() throws Exception {
        String userId = obtainUserId();
        mockMvc.perform(get("/v1/tasks/" + userId)
                        .header("Authorization", "Bearer " + obtainJwtToken())
                        .header("Content-Type", "application/json;type=owner"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("tasks")));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    public void testGetPerformerTasks() throws Exception {
        String userId = obtainUserId();
        mockMvc.perform(get("/v1/tasks/" + userId)
                        .header("Authorization", "Bearer " + obtainJwtToken())
                        .header("Content-Type", "application/json;type=performer"))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("tasks")));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    public void testGetFilteredTasks_withAllParameters() throws Exception {
        String userId = obtainUserId();
        mockMvc.perform(get("/v1/tasks/filter")
                        .header("Authorization", "Bearer " + obtainJwtToken())
                        .param("taskname", "TestTask")
                        .param("owner", userId)
                        .param("priority", "HIGH")
                        .param("status", "INPROGRESS")
                        .param("page", "0")
                        .param("size", "10"))
                        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    public void testGetFilteredTasks_withSomeParameters() throws Exception {
        mockMvc.perform(get("/v1/tasks/filter")
                        .header("Authorization", "Bearer " + obtainJwtToken())
                        .param("statsus", "TODO")
                        .param("priority", "HIGH"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("_embedded.taskModels").isArray())
                        .andExpect(jsonPath("page.size").value(10))
                        .andExpect(jsonPath("page.number").value(0));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    public void testGetFilteredTasks_withNoParameters() throws Exception {
        mockMvc.perform(get("/v1/tasks/filter")
                        .header("Authorization", "Bearer " + obtainJwtToken()))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("tasks")));
    }


    @Test
    @WithMockUser(username = "user@example.com")
    public void testUpdateTaskStatus() throws Exception {
        String taskId = createTaskAndGetId();
        TaskStatusUpdateRequest taskStatusUpdateRequest = new TaskStatusUpdateRequest("INPROGRESS");

        mockMvc.perform(put("/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + obtainJwtToken())
                        .header("Content-Type", "application/json;type=status")
                        .content(objectMapper.writeValueAsString(taskStatusUpdateRequest)))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("taskId")));
    }

    @Test
    @WithMockUser(username = "user@example.com")
    public void testUpdateTaskDetails() throws Exception {
        RegistrationRequest registrationRequest = new RegistrationRequest("user2@example.com", "Full Name", "passworD123@");
        mockMvc.perform(post("/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated());

        String taskId = createTaskAndGetId();

        TaskDetailsUpdateRequest taskDetailsUpdateRequest = new TaskDetailsUpdateRequest(
                "user2@example.com", "taskName22", "taskTheme22", "taskDesc22", "LOW");

        mockMvc.perform(put("/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + obtainJwtToken())
                        .header("Content-Type", "application/json;type=details")
                        .content(objectMapper.writeValueAsString(taskDetailsUpdateRequest)))
                        .andExpect(status().isOk())
                        .andExpect(content().string(containsString("taskId")));
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testDeleteTasks() throws Exception {
        String taskId = createTaskAndGetId();
        mockMvc.perform(delete("/v1/tasks/" + taskId)
                        .header("Authorization", "Bearer " + obtainJwtToken()))
                        .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user@example.com", roles = "USER")
    void testCreateAndGetAndDeleteComment() throws Exception {
        String taskId = createTaskAndGetId();
        CommentCreationRequest commentRequest = new CommentCreationRequest("ExampleText");

        MvcResult createResult = mockMvc.perform(post("/v1/comment/" + taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + obtainJwtToken())
                        .content(objectMapper.writeValueAsString(commentRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        CommentCreationResponse creationResponse = objectMapper.readValue(responseBody, CommentCreationResponse.class);
        String commentId = creationResponse.getCommentId();

        mockMvc.perform(get("/v1/comment/" + taskId)
                        .header("Authorization", "Bearer " + obtainJwtToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments[0].commentText").value("ExampleText"))
                .andExpect(jsonPath("$.comments[0].commentId").value(commentId));

        mockMvc.perform(delete("/v1/comment/" + commentId)
                        .header("Authorization", "Bearer " + obtainJwtToken()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/v1/comment/" + taskId)
                        .header("Authorization", "Bearer " + obtainJwtToken()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments").isEmpty());
    }


    private String obtainJwtToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "passworD123@");
        MvcResult result = mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                        .andExpect(status().isOk())
                        .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JWTResponse jwtResponse = objectMapper.readValue(responseBody, JWTResponse.class);
        return jwtResponse.getToken();
    }

    private String obtainUserId() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "passworD123@");
        MvcResult result = mockMvc.perform(post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        System.out.println(responseBody);
        UserModel userModel = objectMapper.readValue(responseBody, UserModel.class);
        System.out.println(userModel.toString());
        return String.valueOf(userModel.getUserId());
    }

    private String createTaskAndGetId() throws Exception {
        TaskCreationRequest taskCreationRequest = new TaskCreationRequest("TaskName5", "taskTheme5", "taskDesc5", "MEDIUM");

        MvcResult result = mockMvc.perform(post("/v1/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + obtainJwtToken())
                        .content(objectMapper.writeValueAsString(taskCreationRequest)))
                        .andExpect(status().isCreated())
                        .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        TaskCreationResponse taskResponse = objectMapper.readValue(responseBody, TaskCreationResponse.class);
        return String.valueOf(taskResponse.getTaskId());
    }
}
