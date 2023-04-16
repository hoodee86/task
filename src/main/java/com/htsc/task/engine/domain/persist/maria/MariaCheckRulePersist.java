package com.htsc.task.engine.domain.persist.maria;

import com.htsc.task.engine.domain.checkrule.CompleteCheckRule;
import com.htsc.task.engine.domain.checkrule.impl.PercentageCheckRule;
import com.htsc.task.engine.domain.persist.CheckRulePersist;
import com.htsc.task.engine.domain.persist.maria.repository.PropConfigRepository;
import com.htsc.task.engine.domain.persist.maria.entity.PropConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MariaCheckRulePersist implements CheckRulePersist {

    @Autowired
    private PropConfigRepository propConfigRepository;

    @Override
    public void save(CompleteCheckRule checkRule) {
        if (checkRule instanceof PercentageCheckRule) {
            PercentageCheckRule rule = (PercentageCheckRule) checkRule;
            PropConfigEntity entity = new PropConfigEntity(
                    rule.getId(),
                    "CHECKRULE",
                    rule.getName(),
                    rule.toString()
            );
            propConfigRepository.save(entity);
        }
    }

    @Override
    public void query(String id, CompleteCheckRule checkRule) {
        //propConfigRepository.
    }

}
