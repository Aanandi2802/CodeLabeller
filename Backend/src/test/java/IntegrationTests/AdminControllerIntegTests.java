//package IntegrationTests;
//
//import com.csci5308.codeLabeller.Controller.AdminController;
//import com.csci5308.codeLabeller.Models.CodeAnnotations;
//import com.csci5308.codeLabeller.Models.CodeSurvey;
//import com.csci5308.codeLabeller.Repsoitory.SurveyRepository;
//import com.csci5308.codeLabeller.Service.AnnotationService;
//import org.hamcrest.Matchers;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//@WebMvcTest(AdminController.class)
//@ExtendWith(SpringExtension.class)
//@Import(TestConfig.class)
//public class AdminControllerIntegTests {
//
//    @Mock
//    SurveyRepository surveyRepository;
//
//    @InjectMocks
//    AdminController adminController;
//
//    @Autowired
//    AnnotationService annotationService;
//
//    private MockMvc mockMvc;
//
//    @BeforeEach
//    public void setup(){
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
//    }
//
//    @Test
//    public void getAllAnnotationsTest() throws Exception {
//        String username = "johndoe";
//        long surveyId = 1;
//        Authentication authentication = Mockito.mock(Authentication.class);
//        Mockito.when(authentication.getName()).thenReturn(username);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        CodeSurvey codeSurvey = new CodeSurvey();
//        Mockito.when(surveyRepository.findById(surveyId)).thenReturn(Optional.of(codeSurvey));
//
//        Set<CodeAnnotations> codeAnnotationsSet = new HashSet<>();
//        codeSurvey.setAnnotationList(codeAnnotationsSet);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/admin/johndoe/survey/1/annotation/all/"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.size()", Matchers.is(1)));
//    }
//}
