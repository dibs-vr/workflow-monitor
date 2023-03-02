package com.dibin.workflow.workflowmonitor.dao.impl;

import com.dibin.workflow.workflowmonitor.dao.WorkflowDao;
import com.dibin.workflow.workflowmonitor.model.State;
import com.dibin.workflow.workflowmonitor.model.Workflow;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class WorkflowJDBCDaoImpl implements WorkflowDao {

    @Autowired
    private DataSource dataSource;

    public long saveWorkflow(Workflow workflow) {
        String sql = "INSERT INTO WORKFLOW(WORKFLOW_ID, START_TIME, INITIATOR, MODEL_ID, PAYLOADTYPE, PAYLOAD, STATE_ID, CREATED_DATE VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, workflow.getId());
            ps.setTimestamp(2, getTimestamp(workflow.getStartTime(), "EEE MMM d HH:mm:ss z yyyy"));
            ps.setString(3, workflow.getInitiator());
            ps.setInt(4, workflow.getModelId());
            ps.setString(5, workflow.getPayloadType());
            ps.setString(6, workflow.getPayload());
            ps.setInt(7, State.valueOf(workflow.getState()).ordinal() + 1);
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                return rs.getInt(1);
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, ps);
        }
        return -1;
    }

    public int getModelId(String modelName) {
        String sql = "select ID from model where MODEL_ID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, modelName);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                return rs.getInt("ID");
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, ps);
        }
        return -1;
    }

    public int saveModel(String modelName) {
        String sql = "INSERT INTO MODEL(MODEL_ID) VALUES (?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, modelName);
            return ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, ps);
        }
        return -1;
    }

    public void saveWorkflowDetails(long workflowID, int stateId, int modelId) {
        String sql = "INSERT INTO WF_STATE(WF_ID, STATE_ID, MODEL_ID, CREATED_DATE) VALES (?,?,?,?)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, workflowID);
            ps.setInt(2, stateId);
            ps.setInt(3, modelId);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, ps);
        }
    }

    public int isWorkflowSaved(String workflowId) {
        String sql = "SELECT ID FROM WORKFLOW WHERE WORKFLOW_ID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, workflowId);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                return resultSet.getInt("ID");
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, ps);
        }
        return -1;
    }

    public boolean isWorkflowDetailsSaved(long workflowId, int stateId) {
        String sql = "SELECT ID FROM WF_STATE WHERE WF_ID = ? AND STATE_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setLong(1, workflowId);
            ps.setInt(2, stateId);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, ps);
        }
        return false;
    }

    private void close(Connection conn, PreparedStatement ps) {
        if(conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(ps != null) {
            try {
                ps.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Timestamp getTimestamp(String time, String format) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date parsedDate = dateFormat.parse(time);
        return new Timestamp(parsedDate.getTime());
    }
}
