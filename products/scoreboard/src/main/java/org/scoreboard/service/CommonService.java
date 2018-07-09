package org.scoreboard.service;

import java.text.DecimalFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import org.scoreboard.dao.CommonDao;
import org.scoreboard.utils.TimeUtils;

@Service
public class CommonService {
	@Resource
	private CommonDao commonDao;

	/**
	 * 产生各表主键流水号的方法
	 * 
	 * @param sequenceNum
	 *            序列号
	 * @param prefix
	 *            对应表定义的前缀,如交易表的前缀为"JY",充值表的前缀为"CZ"
	 * @return 流水号
	 * @exception DatabaseException
	 *                当查询Sequence number时,有可能抛出<code>DatabaseException</code>
	 */
	public String initialSequenceId(String prefix) {
		long sequenceNum = commonDao.querySeqID();
		return initialSequenceId(prefix, sequenceNum);
	}

	private String initialSequenceId(String prefix, long id) {
		String prefixToAdd = prefix == null ? "" : prefix;
		String sequenceNumFormatted = formatSequence(id);
		return TimeUtils.format(new Date(), "yyyyMMddHH") + prefixToAdd
				+ sequenceNumFormatted;
	}

	/**
	 * 
	 * 对序列号进行初始化，如果>8位则不管，如果<8位，则左补0
	 * 
	 * @param numberToFormat
	 * @return
	 */
	public static String formatSequence(long numberToFormat) {
		DecimalFormat format = new DecimalFormat("00000000");
		return format.format(numberToFormat);
	}

}
