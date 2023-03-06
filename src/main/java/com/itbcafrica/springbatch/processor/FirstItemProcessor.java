package com.itbcafrica.springbatch.processor;

import com.itbcafrica.springbatch.model.StudentJdbc;
import com.itbcafrica.springbatch.model.StudentJson;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<StudentJdbc, StudentJson> {
    @Override
    public StudentJson process(StudentJdbc item) throws Exception {
        System.out.println ( "Inside Item Processor" );
        StudentJson studentJson = new StudentJson ();
        studentJson.setEmail ( item.getEmail () );
        studentJson.setId ( item.getId () );
        studentJson.setFirstName ( item.getFirstName () );
        studentJson.setLastName ( item.getLastName () );
        return studentJson;
    }
}
