package onlineShop.log;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// payment method
@Component
public class PaymentAction {
	//Autowired关键字必须要有
	@Autowired
	private Logger logger;
//BigDecimal： 大额数据单位
	public void pay(BigDecimal payValue) {
		logger.log("pay begin, payValue is " + payValue);
		logger.log("pay end");
	}
}

