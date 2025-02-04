package com.justjournal.ctl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/@")
public class ActivityStreamController {
    private static final String PATH_USERNAME = "username";

    @Autowired
    ObjectMapper mapper;

    @GetMapping(value = "{username}", produces = "application/activity+json")
    @ResponseBody
    public String get(@PathVariable(PATH_USERNAME) final String username, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        var activityStreamResponse = new ActivityStreamResponse();

        return mapper.writeValueAsString(activityStreamResponse);
    }

    @Getter
    @Setter
    class ActivityStreamResponse {

        @JsonProperty("@context")
        private List<String> context;

        @JsonCreator
        public ActivityStreamResponse() {
            this.context = new ArrayList<>();
            context.add("https://www.w3.org/ns/activitystreams");
            context.add("https://w3id.org/security/v1");
        }

    }
}


//{"@context":["https:\/\/www.w3.org\/ns\/activitystreams","https:\/\/w3id.org\/security\/v1",{"manuallyApprovesFollowers":"as:manuallyApprovesFollowers","toot":"http:\/\/joinmastodon.org\/ns#","featured":{"@id":"toot:featured","@type":"@id"},"featuredTags":{"@id":"toot:featuredTags","@type":"@id"},"alsoKnownAs":{"@id":"as:alsoKnownAs","@type":"@id"},"movedTo":{"@id":"as:movedTo","@type":"@id"},"schema":"http:\/\/schema.org#","PropertyValue":"schema:PropertyValue","value":"schema:value","discoverable":"toot:discoverable","Device":"toot:Device","Ed25519Signature":"toot:Ed25519Signature","Ed25519Key":"toot:Ed25519Key","Curve25519Key":"toot:Curve25519Key","EncryptedMessage":"toot:EncryptedMessage","publicKeyBase64":"toot:publicKeyBase64","deviceId":"toot:deviceId","claim":{"@type":"@id","@id":"toot:claim"},"fingerprintKey":{"@type":"@id","@id":"toot:fingerprintKey"},"identityKey":{"@type":"@id","@id":"toot:identityKey"},"devices":{"@type":"@id","@id":"toot:devices"},"messageFranking":"toot:messageFranking","messageType":"toot:messageType","cipherText":"toot:cipherText","suspended":"toot:suspended","Emoji":"toot:Emoji","focalPoint":{"@container":"@list","@id":"toot:focalPoint"}}],"id":"https:\/\/bugle.lol\/@bugle","type":"Person","following":"https:\/\/bugle.lol\/@bugle\/following","followers":"https:\/\/bugle.lol\/@bugle\/followers","inbox":"https:\/\/bugle.lol\/@bugle\/inbox","outbox":"https:\/\/bugle.lol\/@bugle\/outbox","featured":"https:\/\/bugle.lol\/@bugle\/collections\/featured","featuredTags":"https:\/\/bugle.lol\/@bugle\/collections\/tags","preferredUsername":"bugle","name":"bugle dot lol","summary":"<p>Bugle is minimal ActivityPub server built with Laravel<\/p>","url":"https:\/\/bugle.lol\/@bugle","manuallyApprovesFollowers":false,"discoverable":true,"published":"2023-06-23T21:58:48.000000Z","publicKey":{"id":"https:\/\/bugle.lol\/@bugle\/#main-key","owner":"https:\/\/bugle.lol\/@bugle","publicKeyPem":"-----BEGIN PUBLIC KEY-----\nMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0uhSYBppOsoeYi9NNzgG\nUG12H\/o+Hd9Trh+af7T1PzUxHFiAnA5Fveq69Sfxg1Y6saUH+7cah9uFi9ZWWcqq\nehNOTKkvT0WLYWPWBi95Ri0wrerGo0Q+DRLZpSYi9KLgIoTU+f4MIbTbfvy4hUJt\n1ukSana2WSe1QtpA\/X0KK\/72W05yw1f0lU98D2t4M\/w1D0p8g9r773KH1w\/nqyNX\nMA4C241ym+Hs7uS4ygen+xOyonjCKCQBbKvDAd84QHKHYzvseptv0OI3+XRvYo\/2\nhJU4lEGAWyva8LzdEEwfT8bU9Lw7GI+LGayXZHz+E4cxg+RE4pAVF7+zYqMm1j6S\n\/wIDAQAB\n-----END PUBLIC KEY-----\n"},"tag":[],"attachment":[{"type":"PropertyValue","name":"Website","value":"<a href=\"https:\/\/bugle.lol\" target=\"_blank\" rel=\"nofollow noopener noreferrer me\"><span class=\"invisible\">https:\/\/<\/span><span class=\"\">bugle.lol<\/span><span class=\"invisible\"><\/span><\/a>"},{"type":"PropertyValue","name":"Made by Robb Knight","value":"<a href=\"https:\/\/social.lol\/@robb\" target=\"_blank\" rel=\"nofollow noopener noreferrer me\"><span class=\"invisible\">https:\/\/<\/span><span class=\"\">social.lol\/@robb<\/span><span class=\"invisible\"><\/span><\/a>"},{"type":"PropertyValue","name":"GitHub","value":"<a href=\"https:\/\/github.com\/rknightuk\/bugle\" target=\"_blank\" rel=\"nofollow noopener noreferrer me\"><span class=\"invisible\">https:\/\/<\/span><span class=\"\">github.com\/rknightuk\/bugle<\/span><span class=\"invisible\"><\/span><\/a>"}],"endpoints":{"sharedInbox":"https:\/\/bugle.lol\/inbox"},"icon":{"type":"Image","mediaType":"image\/jpeg","url":"https:\/\/bugledotlol.s3.amazonaws.com\/avatars\/bugle-1697209182.png"},"image":{"type":"Image","mediaType":"image\/jpeg","url":"https:\/\/bugledotlol.s3.amazonaws.com\/headers\/bugle-1687730539-.jpg"}}