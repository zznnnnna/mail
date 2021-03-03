
    import javax.activation.DataHandler;
    import javax.activation.DataSource;
    import javax.activation.FileDataSource;
    import javax.mail.*;
    import javax.mail.internet.*;
    import java.io.File;
    import java.io.UnsupportedEncodingException;
    import java.security.GeneralSecurityException;
    import java.util.Properties;

    public class SendEmail
    {
        public static void main(String[] args) throws MessagingException, UnsupportedEncodingException {
           String email="z@zhaozenan.com";//收件人
           String msg = "测试功能";//邮件正文内容
           String subject = "邮件主题";//邮件主题
            File affix = new File("C:\\Users\\z\\Desktop\\mail附件.txt");
            sendMail(email,subject,msg,affix);
        }
        public static void sendMail(String email, String subject, String emailMsg, File attachment)
                throws AddressException, MessagingException, UnsupportedEncodingException {
            // 1.创建一个程序与邮件服务器会话对象 Session
            Properties props = new Properties();
            props.setProperty("mail.transport.protocol", "SMTP");// 发邮件的协议
            // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
            // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com 腾讯: smtp.qq.com
            props.setProperty("mail.host", "smtp.qq.com"); // 发送邮件的服务器地址
            // 需要请求认证
            props.setProperty("mail.smtp.auth", "true");// 指定验证为true

            // 创建验证器
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
                    // 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
                    // 对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）
                    return new PasswordAuthentication("88888888@qq.com", "lavzywaejylkbgjd");// 发邮件的账号的验证
                }
            };

            Session session = Session.getInstance(props, auth);
            // 2.创建一个Message，它相当于是邮件内容
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("88888888@qq.com")); // 设置发送者

            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email)); // 设置发送方式与接收者

            message.setSubject(subject);// 设置主题
            // message.setText("这是一封激活邮件，请<a href='#'>点击</a>");
            // 设置邮件内容
            //收件人email
            // 添加附件的内容
            //向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(emailMsg, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));

                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }
            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            //message.setFileName();
            //发送纯文本邮件用这个即可
            //message.setContent(emailMsg, "text/html;charset=utf-8");
            // 3.创建 Transport用于将邮件发送
            Transport.send(message);
        }
    }