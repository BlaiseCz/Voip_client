package microphone_tests;

import com.google.common.collect.Lists;
import com.models.RegistrationForm;
import com.models.UserTO;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiProviderTest {
    final String url = "https://voiptipchat.herokuapp.com//users";
    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void initRestTemplate() {
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
    }


    @Test
    public void registerTest() {
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        String url = "https://voiptipchat.herokuapp.com/users/register";


        RegistrationForm registerForm = RegistrationForm
                .builder()
                .password("asdasasddasd3")
                .email("duasdpa@xd.pl")
                .IPAddress("dupadupa")
                .username("asdasdas3d")
                .build();

        System.out.println(registerForm);
        UserTO xd = restTemplate.postForObject(url, registerForm, UserTO.class);
        System.out.println(xd);
        System.out.println(xd);
    }

    @Test
    public void obtainAll() {
        String endpointUrl = url + "/all";

        List<UserTO> users = Lists.newArrayList(restTemplate.getForObject(endpointUrl, UserTO[].class));
        System.out.println(users);
    }
}
