package mx.fortson.rehab.utils;

import java.sql.SQLException;

import mx.fortson.rehab.DatabaseDegens;
import mx.fortson.rehab.bean.DuelResultBean;

public class DuelUtils {
	
	private static DuelResultBean duel(String participant1, String participant2, Long stakeAmount) {
		DuelResultBean result = new DuelResultBean();
		result.setAmount(stakeAmount);
		result.setWinner(RandomUtils.randomStringFromArray(new String[] {participant1,participant2}));
		result.setLoser(result.getWinner().equals(participant1) ? participant2 : participant1);
		return result;
	}

	public static DuelResultBean randomDuel(long idLong) {
		DuelResultBean result = new DuelResultBean();
		try {
			
			Long funds = DatabaseDegens.getFundsById(idLong);
			if(null==funds) {
				result.setFlavourText("You need to register first.");
			}else if(funds>0) {
				boolean win = true;
				int degenId = DatabaseDegens.getDegenId(idLong);
				Long stakeAmount = RandomUtils.randomAmountFromRange(new Long[] {1L,funds});
				result = duel("A rando", "<@" + String.valueOf(idLong) + ">", stakeAmount);
				
				Long newFunds = funds;
				if(result.getLoser().equals("<@" + String.valueOf(idLong) + ">")) {
					win = false;
					newFunds = newFunds - stakeAmount;
					
					int freeFarms = freeFarmChance(stakeAmount,DatabaseDegens.getDegenId(idLong));
					if(freeFarms>0) {
						result.setFreeFarm(true);
						result.setFreeFarms(freeFarms);
					}
				}else {
					newFunds = newFunds + stakeAmount;
				}
				DatabaseDegens.updateRate(idLong, win);
				DatabaseDegens.updateFunds(newFunds, degenId);
				
			}else {
				result.setFlavourText("You don't have the funds to duel anybody.");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private static int freeFarmChance(Long stakeAmount, int... ids) throws SQLException {
		int result = 0;
		if(stakeAmount>200000000L) {
			Long random = RandomUtils.randomAmountFromRange(new Long[] {0L,10000L});
			if(random.compareTo(1000L)<0) {
				result = RandomUtils.randomFreeFarm();
				
				DatabaseDegens.addFarmAtt(result,ids);
			}
		}
		return result;
	}

	public static DuelResultBean randomDuelSetAmount(long idLong, Long stakeAmount) {
		DuelResultBean result = new DuelResultBean();
		try {
			
			Long funds = DatabaseDegens.getFundsById(idLong);
			if(stakeAmount == -1L) {
				stakeAmount = funds;
			}
			if(null==funds) {
				result.setFlavourText("You need to register first.");
			}else if(funds >= stakeAmount) {
				boolean win = true;
				int degenId = DatabaseDegens.getDegenId(idLong);
				
				result = duel("A rando", "<@" + String.valueOf(idLong) + ">", stakeAmount);
				
				Long newFunds = funds;
				if(result.getLoser().equals("<@" + String.valueOf(idLong) + ">")) {
					win = false;
					newFunds = newFunds - stakeAmount;
					
					int freeFarms = freeFarmChance(stakeAmount,DatabaseDegens.getDegenId(idLong));
					if(freeFarms>0) {
						result.setFreeFarm(true);
						result.setFreeFarms(freeFarms);
					}
				}else {
					newFunds = newFunds + stakeAmount;
				}
				DatabaseDegens.updateFunds(newFunds, degenId);
				DatabaseDegens.updateRate(idLong, win);
			}else {
				result.setFlavourText("No cheating, you don't have enough to send that much.");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public static DuelResultBean giftChuck(Long gifterId, Long giftedId, Long giftChuckAmount) {
		DuelResultBean result = new DuelResultBean();
		try {
			if(gifterId.equals(giftedId)) {
				result.setFlavourText("That's just called staking dummy.");
			}else {
				
				Long gifterFunds = DatabaseDegens.getFundsById(gifterId);
				if(null==gifterFunds) {
					result.setFlavourText("You need to register first.");
				}else if(gifterFunds >= giftChuckAmount) {
					boolean win = true;
					int gifterDegenId = DatabaseDegens.getDegenId(gifterId);
					int giftedDegenId = DatabaseDegens.getDegenId(giftedId);
					
					result = duel("A rando", "<@" + String.valueOf(giftedId) + ">", giftChuckAmount);
					
					if(result.getLoser().equals("<@" + String.valueOf(giftedId) + ">")) {
						win = false;
						gifterFunds = gifterFunds - giftChuckAmount;
						DatabaseDegens.updateFunds(gifterFunds, gifterDegenId);
						
						int gifterIdInt = DatabaseDegens.getDegenId(gifterId);
						int giftedIdInt = DatabaseDegens.getDegenId(giftedId);
						int freeFarms = freeFarmChance(giftChuckAmount,gifterIdInt,giftedIdInt);
						if(freeFarms>0) {
							result.setFreeFarm(true);
							result.setFreeFarms(freeFarms);
						}
						DatabaseDegens.updateRate(giftedId, win);
					}else {
						Long giftedFunds = DatabaseDegens.getFundsById(giftedId);
						giftedFunds = giftedFunds + giftChuckAmount;
						DatabaseDegens.updateFunds(giftedFunds, giftedDegenId);
					}
				}else {
					result.setFlavourText("That's generous of you, but you can't gift what you don't have.");
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static DuelResultBean duel(Long challengerId, Long challengedId, Long duelAmount) {
		DuelResultBean result = new DuelResultBean();
		try {
			
			Long challengerFunds = DatabaseDegens.getFundsById(challengerId);
			Long challengedFunds = DatabaseDegens.getFundsById(challengedId);
			if(null==challengerFunds || null==challengedFunds) {
				result.setFlavourText("You need to register first.");
			}else if(challengerFunds >= duelAmount) {
				if(challengedFunds >= duelAmount) {
					int challengerDegenId = DatabaseDegens.getDegenId(challengerId);
					int challengedDegenId = DatabaseDegens.getDegenId(challengedId);
					
					result = duel("<@" + String.valueOf(challengerId) + ">", "<@" + String.valueOf(challengedId) + ">", duelAmount);
					
					if(result.getLoser().equals("<@" + String.valueOf(challengerId) + ">")) {
						DatabaseDegens.updateFundsSum(-duelAmount, challengerDegenId);
						DatabaseDegens.updateFundsSum(duelAmount, challengedDegenId);
						
						int freeFarms = freeFarmChance(duelAmount,challengerDegenId);
						if(freeFarms>0) {
							result.setFreeFarm(true);
							result.setFreeFarms(freeFarms);
						}
						DatabaseDegens.updateRate(challengedId, true);
						DatabaseDegens.updateRate(challengerId, false);
					}else {
						DatabaseDegens.updateFundsSum(duelAmount, challengerDegenId);
						DatabaseDegens.updateFundsSum(-duelAmount, challengedDegenId);
						int freeFarms = freeFarmChance(duelAmount,challengedDegenId);
						if(freeFarms>0) {
							result.setFreeFarm(true);
							result.setFreeFarms(freeFarms);
						}
						DatabaseDegens.updateRate(challengerId, true);
						DatabaseDegens.updateRate(challengedId, false);
					}
				}else {
					result.setFlavourText("Challenged person does not have enough funds.");
				}
			}else {
				result.setFlavourText("Challenger does not have enough funds.");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

}
