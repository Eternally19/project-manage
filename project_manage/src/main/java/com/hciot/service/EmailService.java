package com.hciot.service;

import com.hciot.entity.Process;
import com.hciot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Async
    public void email() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("邮件主题：测试流程邮件");
        message.setText("邮件内容：你的任务已开始");
        message.setTo("1779797461@qq.com");
        message.setFrom("1779797461@qq.com");

        mailSender.send(message);
    }

    @Async
    public void email(Process process) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("任务提醒");
        message.setText("项目："+process.getProject().getTitle()+"\n流程:"+process.getContext()+process.getStatus()+"\n结束时间:"+sdf.format(process.getEndTime()));
        message.setTo(process.getUser().getEmail());
        message.setFrom("1779797461@qq.com");
        mailSender.send(message);
    }

    @Async
    public void saveProcessEmail(Process process) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("邮件主题：新增任务");
        message.setText("项目:"+process.getProject().getTitle()+"\n新增你负责的流程:"+process.getContext()+"\n创建时间:"+sdf.format(process.getCreateTime())
                        +"\n结束时间:"+sdf.format(process.getEndTime())+"\n状态:"+process.getStatus());
        message.setTo(process.getUser().getEmail());
        message.setFrom("1779797461@qq.com");
        mailSender.send(message);
    }

    @Async
    public void redoProcessEmail(Process redoProcess) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("邮件主题：任务重做");
        message.setText("项目:"+redoProcess.getProject().getTitle()+"\n你负责的流程需要重做:"+redoProcess.getContext()+"\n结束时间:"+sdf.format(redoProcess.getEndTime()));
        message.setTo(redoProcess.getUser().getEmail());
        message.setFrom("1779797461@qq.com");
        mailSender.send(message);
    }

    @Async
    public void redoProcessEmail(Process redoProcess, Process process) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("邮件主题：任务变更");
        message.setText("项目:"+redoProcess.getProject().getTitle()+"\n流程重做:"+redoProcess.getContext()+"\n负责人:"+redoProcess.getUser().getUsername()
                +"\n\n你负责的流程:"+process.getContext()+"\n受影响状态变更为"+process.getStatus()+"\n结束时间"+sdf.format(process.getEndTime()));
        message.setTo(process.getUser().getEmail());
        message.setFrom("1779797461@qq.com");
        mailSender.send(message);
    }

    @Async
    public void updateProcessEmail(Process process,User user,String email, String context, Date endTime) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("邮件主题：任务变更");
        message.setText("项目:"+process.getProject().getTitle()+"\n原本:\n流程:"+context+"\n状态为"+process.getStatus()+"\n负责人:"+user.getUsername()+"\n结束时间"+sdf.format(endTime)
                +"\n\n变更为：\n流程:"+process.getContext()+"\n状态为"+process.getStatus()+"\n负责人:"+process.getUser().getUsername()+"\n结束时间"+sdf.format(process.getEndTime()));
        message.setTo(email);
        message.setFrom("1779797461@qq.com");
        mailSender.send(message);
    }

}
