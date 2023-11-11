package io.toolisticon.fluapigen.integrationtest;

import io.toolisticon.fluapigen.api.FluentApi;
import io.toolisticon.fluapigen.api.FluentApiBackingBean;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanField;
import io.toolisticon.fluapigen.api.FluentApiBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiCommand;
import io.toolisticon.fluapigen.api.FluentApiImplicitValue;
import io.toolisticon.fluapigen.api.FluentApiInlineBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiInterface;
import io.toolisticon.fluapigen.api.FluentApiParentBackingBeanMapping;
import io.toolisticon.fluapigen.api.FluentApiRoot;
import io.toolisticon.fluapigen.api.MappingAction;
import io.toolisticon.fluapigen.api.TargetBackingBean;
import io.toolisticon.fluapigen.validation.api.Matches;
import io.toolisticon.fluapigen.validation.api.NotNull;

import java.io.File;
import java.util.List;

@FluentApi("EmailFluentApiStarter")
public class EmailFluentApi {

    // ---------------------------------------------------
    // -- Backing Beans
    // ---------------------------------------------------

    /**
     * The root backing bean.
     */
    @FluentApiBackingBean
    public interface EmailBB {

        List<RecipientBB> recipients();

        String subject();

        String body();

        List<AttachmentBB> attachments();
    }

    /**
     * Backing bean for storing recipients
     */
    @FluentApiBackingBean
    public interface RecipientBB{
        RecipientKind recipientKind();

        String emailAddress();
    }

    public enum RecipientKind {
        TO,
        CC,
        BCC;
    }

    /**
     * Backing Bean for attachments
     */
    @FluentApiBackingBean
    public interface AttachmentBB {
        @FluentApiBackingBeanField()
        File attachment();

        String attachmentName();
    }

    // ---------------------------------------------------
    // -- Commands
    // ---------------------------------------------------

    @FluentApiCommand
    public static class SendEmailCommand {
        public static EmailBB sendEmail(EmailBB emailBB) {
            // Implementation for sending the email
            return emailBB;
        }
    }

    // ---------------------------------------------------
    // -- Fluent Api
    // ---------------------------------------------------


    @FluentApiRoot
    @FluentApiInterface(EmailBB.class)
    public interface EmailStartInterface {

        @FluentApiInlineBackingBeanMapping("recipients")
        @FluentApiImplicitValue(value="TO", id="recipientKind", target = TargetBackingBean.INLINE)
        AddRecipientsOrSetSubject to (
                @FluentApiBackingBeanMapping(value = "emailAddress", target = TargetBackingBean.INLINE)
                @NotNull @Matches(".*[@].*") String emailAddress);

        @FluentApiInlineBackingBeanMapping("recipients")
        @FluentApiImplicitValue(value="CC", id="recipientKind", target = TargetBackingBean.INLINE)
        AddRecipientsOrSetSubject cc (@FluentApiBackingBeanMapping(value = "emailAddress", target = TargetBackingBean.INLINE)
                                      @NotNull @Matches(".*[@].*") String emailAddress);
        @FluentApiInlineBackingBeanMapping("recipients")
        @FluentApiImplicitValue(value="BCC", id="recipientKind", target = TargetBackingBean.INLINE)
        AddRecipientsOrSetSubject bcc (@FluentApiBackingBeanMapping(value = "emailAddress", target = TargetBackingBean.INLINE)
                                       @NotNull @Matches(".*[@].*") String emailAddress);
    }

    @FluentApiInterface(EmailBB.class)
    public interface AddRecipientsOrSetSubject {
        EmailStartInterface and();

        AddBodyInterface withSubject (@FluentApiBackingBeanMapping(value="subject") @NotNull String subject);

    }

    @FluentApiInterface(EmailBB.class)
    public interface AddBodyInterface {

        AddAttachmentOrCloseCommandInterface withBody(@FluentApiBackingBeanMapping(value="subject") @NotNull String body) ;

    }

    @FluentApiInterface(EmailBB.class)
    public interface AddAttachmentOrCloseCommandInterface {

        AddAttachmentInterface addAttachment();


        @FluentApiCommand(SendEmailCommand.class)
        EmailBB sendEmail();

    }


    @FluentApiInterface(AttachmentBB.class)
    public interface AddAttachmentFileInterface {

        @FluentApiParentBackingBeanMapping(value = "attachments", action = MappingAction.ADD)
        AddAttachmentOrCloseCommandInterface fromFile(@FluentApiBackingBeanMapping(value="attachment") File file);
    }

    @FluentApiInterface(AttachmentBB.class)
    public interface AddAttachmentInterface extends AddAttachmentFileInterface{

        AddAttachmentFileInterface withCustomName (@FluentApiBackingBeanMapping(value="attachmentName") String attachmentName);

    }



}
