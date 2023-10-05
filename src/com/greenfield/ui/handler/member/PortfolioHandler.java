/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.greenfield.ui.handler.member;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.PortfolioConstants;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.dao.member.PortfolioDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.ui.cache.DBCachePool;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.member.PortfolioInfo;
import com.greenfield.common.object.member.PortfolioVO;
import com.greenfield.common.object.member.PositionInfo;
import com.greenfield.common.object.member.TransactionInfo;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.user.User;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.IDGenerator;
import com.greenfield.common.util.NumberUtil;

import java.util.Vector;

/**
 * 
 * @author Qin
 */
public class PortfolioHandler extends BaseHandler {
	private final static String DEFAULT_FOLDER_NAME = "My First Portfolio";

	private PortfolioDAO portfolioDao = null;
	private StockAdminDAO stockDao = null;

	/**
	 * Make sure this person is operating his/her own portfolio.
	 */
	protected boolean preSecurityCheck(User user, BaseObject obj, WebSessionContext sessionContext) {
		PortfolioVO portfolioVO = (PortfolioVO) obj;
		String portfolioId = portfolioVO.getPortfolioId();
		boolean pass = false;
		if (portfolioId != null && !portfolioId.equals("")) {
			try {
				PortfolioDAO portfolioDaoLoc = new PortfolioDAO();
				portfolioDaoLoc.setDatabase(database);
				Vector portfolioList = DBCachePool.getPortfolioListFromDB(
						user.getUserId(), portfolioDaoLoc);
				if (portfolioList != null) {
					for (int i = 0; i < portfolioList.size(); i++) {
						PortfolioInfo portfolioInfo = (PortfolioInfo) portfolioList
								.get(i);
						if (portfolioId.equals(portfolioInfo.getPortfolioId())) {
							pass = true;
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			pass = true;
		}

		if (pass == false) {
			System.out.println("Warning: User " + user.getUserId()
					+ " is trying to operate portfolio: " + portfolioId);
		}

		return pass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.greenfield.ui.handler.BaseHandler#doAction(com.greenfield.common.object
	 * .BaseObject)
	 */
	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		PortfolioVO portfolioVO = (PortfolioVO) obj;
		String mode = portfolioVO.getMode();
		String type = portfolioVO.getType();
		portfolioVO.setSuccess(true);
		String currentDate = DateUtil.getDateStringInYYYYMMDDFormat(null);

		try {
			portfolioDao = new PortfolioDAO();
			stockDao = new StockAdminDAO();
			portfolioDao.setDatabase(database);
			stockDao.setDatabase(database);

			if (mode != null && mode.equals("newportfolio")) {
				if (type != null && !type.equals("blank")) {
					addPortfolio(portfolioVO, currentDate);
				}

				// portfolioList is used when you do new portfolio in blank case
				Vector portfolioList = DBCachePool.getPortfolioListFromDB(
						portfolioVO.getUserId(), portfolioDao);
				portfolioVO.setPortfolioList(portfolioList);

			} else if (mode != null && mode.equals("updateportfolio")) {
				String portfolioId = portfolioVO.getPortfolioId();
				// System.out.println("loaded the portfolio: " +
				// portfolioVO.getPortfolioId());

				if (type != null && type.equals("delete")) {
					// remove everything
					portfolioDao.deletePortfolioAndRelatedFromDB(portfolioId);
					
					// reset the cache, and forward to customer home then
					DBCachePool.resetPortfolioListFromDB(portfolioVO.getUserId(),
							portfolioDao);
						
				} else if (type != null && type.equals("update")) {
					updatePortfolio(portfolioVO, currentDate);

					// need to load portfolio info here
					PortfolioInfo portfolioInfoInDB = portfolioDao
							.getPortfolioEntryFromDB(portfolioId);
					portfolioVO.setPortfolioInfo(portfolioInfoInDB);

					// load all the current open positions - as the page is
					// going to show the positions
					Vector positionList = portfolioDao
							.getPortfolioPositionsListFromDB(portfolioId,
									PortfolioConstants.OPEN_POSITION);
					// find gain/lost for all the positions
					double investValue = calculateGain(positionList);
					portfolioVO.setInvestValue(investValue);
					portfolioVO.setTotalValue(portfolioVO.getPortfolioInfo()
							.getCurrentCash() + investValue);
					portfolioVO.setPositionList(positionList);

				} else {
					// need to load the portfolio from DB for update purpose -
					// beginnig blank form
					// System.out.println("loaded the portfolio: " +
					// portfolioVO.getPortfolioId());
					PortfolioInfo portfolioInfoInDB = portfolioDao
							.getPortfolioEntryFromDB(portfolioVO
									.getPortfolioId());
					portfolioVO.setPortfolioInfo(portfolioInfoInDB);

					Vector portfolioList = DBCachePool.getPortfolioListFromDB(
							portfolioVO.getUserId(), portfolioDao);
					portfolioVO.setPortfolioList(portfolioList);
				}

				// may need to load stuff here if page needs

			} else if (mode != null && mode.equals("viewtransactions")) {
				// get all the transactions for a given portfolio
				String portfolioId = portfolioVO.getPortfolioId();

				if (portfolioId != null && !portfolioId.equals("")) {
					// load the transactions
					Vector list = portfolioDao
							.getPortfolioTransactionsListFromDB(portfolioId);
					portfolioVO.setTransactionList(list);

					// also load the portfolio info to the VO
					PortfolioInfo portfolioInfoInDB = portfolioDao
							.getPortfolioEntryFromDB(portfolioVO
									.getPortfolioId());
					portfolioVO.setPortfolioInfo(portfolioInfoInDB);
				} else {
					portfolioVO.setTransactionList(new Vector());
				}

			} else if (mode != null && mode.equals("newposition")) {
				String portfolioId = portfolioVO.getPortfolioId();

				// if portfolioId is not given, then should be error
				if (portfolioId != null && !portfolioId.equals("")) {
					// need to load portfolio info here
					PortfolioInfo portfolioInfoInDB = portfolioDao
							.getPortfolioEntryFromDB(portfolioId);
					portfolioVO.setPortfolioInfo(portfolioInfoInDB);
					
					if (type != null && !type.equals("blank")) {
						// if it is not blank, so it is "add", then call to add
						// the position
						int success = addPosition(portfolioVO, currentDate);

						if (success != 0) {
							// reload it 
							portfolioInfoInDB = portfolioDao
									.getPortfolioEntryFromDB(portfolioId);
							portfolioVO.setPortfolioInfo(portfolioInfoInDB);
							
							// load all the current open positions - as the page
							// is going to show the positions
							Vector positionList = portfolioDao
									.getPortfolioPositionsListFromDB(
											portfolioId,
											PortfolioConstants.OPEN_POSITION);
							// find gain/lost for all the positions
							double investValue = calculateGain(positionList);
							portfolioVO.setInvestValue(investValue);
							portfolioVO.setTotalValue(portfolioVO
									.getPortfolioInfo().getCurrentCash()
									+ investValue);
							portfolioVO.setPositionList(positionList);

							portfolioVO.setSuccess(true);
						} else {
							portfolioVO.setSuccess(false);
						}
					} 

				}
			} else if (mode != null && mode.equals("updateposition")) {
				String portfolioId = portfolioVO.getPortfolioId();

				// if portfolioId is not given, then should be error
				if (portfolioId != null && !portfolioId.equals("")) {
					if (type != null && !type.equals("blank")) {
						// if it is not blank, then call to update position
						updatePosition(portfolioVO, currentDate);

						// need to load portfolio info here
						PortfolioInfo portfolioInfoInDB = portfolioDao
								.getPortfolioEntryFromDB(portfolioId);
						portfolioVO.setPortfolioInfo(portfolioInfoInDB);

						// load all the current open positions - as the page is
						// going to show the positions
						Vector positionList = portfolioDao
								.getPortfolioPositionsListFromDB(portfolioId,
										PortfolioConstants.OPEN_POSITION);

						// find gain/lost for all the positions
						double investValue = calculateGain(positionList);
						portfolioVO.setInvestValue(investValue);
						portfolioVO.setTotalValue(portfolioVO
								.getPortfolioInfo().getCurrentCash()
								+ investValue);
						portfolioVO.setPositionList(positionList);
					} else {

						// need to load portfolio info here
						PortfolioInfo portfolioInfoInDB = portfolioDao
								.getPortfolioEntryFromDB(portfolioId);
						portfolioVO.setPortfolioInfo(portfolioInfoInDB);

						// load the postion
						PositionInfo positionInfoInDB = portfolioDao
								.getPortfolioPositionEntryFromDB(portfolioVO
										.getPositionInfo().getOrderId(),
										portfolioId);
						portfolioVO.setPositionInfo(positionInfoInDB);
					}

				}

			} else if (mode != null && mode.equals("viewpositions")) {
				// only load the closed position for the portfolio
				Vector positionList = portfolioDao
						.getPortfolioPositionsListFromDB(
								portfolioVO.getPortfolioId(),
								PortfolioConstants.OPEN_POSITION);

				// load the portfolio info
				PortfolioInfo portfolioInfoInDB = portfolioDao
						.getPortfolioEntryFromDB(portfolioVO.getPortfolioId());
				portfolioVO.setPortfolioInfo(portfolioInfoInDB);

				// find gain/lost for all the positions
				double investValue = calculateGain(positionList);
				portfolioVO.setInvestValue(investValue);
				portfolioVO.setTotalValue(portfolioVO.getPortfolioInfo()
						.getCurrentCash() + investValue);
				portfolioVO.setPositionList(positionList);

			} else if (mode != null && mode.equals("viewclosedpositions")) {
				// load the portfolio info
				PortfolioInfo portfolioInfoInDB = portfolioDao
						.getPortfolioEntryFromDB(portfolioVO.getPortfolioId());
				portfolioVO.setPortfolioInfo(portfolioInfoInDB);

				// only load the closed position for the portfolio
				Vector positionList = portfolioDao
						.getPortfolioPositionsListFromDB(
								portfolioVO.getPortfolioId(),
								PortfolioConstants.CLOSED_POSITION);

				// find gain/lost for all the positions
				calculateGain(positionList);

				portfolioVO.setPositionList(positionList);

			} else if (mode != null && mode.equals("stockinfoajax")) {
				// get stock info for ajax call
				boolean found = false;
				PositionInfo positionInfo = new PositionInfo();
				String ticker = portfolioVO.getPositionInfo().getTicker();
				if (ticker != null) {
					System.out.println("ticker: " + ticker);
					Vector stkList = stockDao.getStockListFromDB(null,
							ticker.toUpperCase(), null);
					if (stkList != null && stkList.size() > 0) {
						// found, but only pick the first one
						found = true;
						Stock stk = (Stock) stkList.get(0);
						positionInfo.setStockId(stk.getStockId());
						positionInfo.setOpenPrice(stk.getPrice());
						positionInfo.setOpenDate(stk.getLastUpdated());
						positionInfo.setCompanyName(stk.getCompanyName());
						portfolioVO.setSuccess(true);
					}
				}
				if (found == false) {
					portfolioVO.setSuccess(false);
				}

				portfolioVO.setPositionInfo(positionInfo);

			} else {
				portfolioVO.setSuccess(false);
				portfolioVO.setMessage("This mode is not handled: " + mode);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// analVO.setDisplay("error");
			portfolioVO.setSuccess(false);
			portfolioVO.setMessage("Error: in program: " + e.getMessage());
		}
		
		if (portfolioVO.getPortfolioList() == null || portfolioVO.getPortfolioList().size() == 0) {
			Vector portfolioList = DBCachePool.getPortfolioListFromDB(
					portfolioVO.getUserId(), portfolioDao);
			portfolioVO.setPortfolioList(portfolioList);
		}

		return portfolioVO;
	}

	private void addPortfolio(PortfolioVO portfolioVO, String currentDate)
			throws Exception {
		String portfolioId = IDGenerator.getNextId(database,
				IDGenerator.PORTFOLIO_ID_PREFIX);
		PortfolioInfo info = portfolioVO.getPortfolioInfo();
		if (info.getCurrentCash() < 0) {
			// do not allow to set negative numbers for current cash
			info.setCurrentCash(0);
		}

		info.setUserId(portfolioVO.getUserId());
		info.setPortfolioId(portfolioId);
		info.setMDate(currentDate);
		portfolioDao.addPortfolioEntryToDB(info);

		// create the transaction to DB for the new folder
		TransactionInfo transInfo = new TransactionInfo();
		transInfo.setTransId(1);
		transInfo.setTransType("A"); // A - add, R - reduce
		transInfo.setPortfolioId(portfolioId);
		transInfo.setMDate(currentDate);
		transInfo.setTransAmount(info.getCurrentCash());
		transInfo.setCurrentCash(info.getCurrentCash());
		portfolioDao.addPortfolioTransactionToDB(transInfo);

		// need to reset the cache
		DBCachePool.resetPortfolioListFromDB(portfolioVO.getUserId(),
				portfolioDao);
	}

	private void updatePortfolio(PortfolioVO portfolioVO, String currentDate)
			throws Exception {
		// regular update, change name, trade_cost, current_cash
		PortfolioInfo info = portfolioVO.getPortfolioInfo();
		if (info.getCurrentCash() < 0) {
			// do not allow to set negative numbers for current cash
			info.setCurrentCash(0);
		}

		String portfolioId = portfolioVO.getPortfolioId();

		if (portfolioId != null && !portfolioId.equals("")) {
			// find what is in DB, so do decide the Currrent Cash is added or
			// reduced
			// only create record to the transaction table if the current cash
			// field is changed
			boolean createTransactionRecord = false;
			TransactionInfo transInfo = new TransactionInfo();

			PortfolioInfo portfolioInfoInDB = portfolioDao
					.getPortfolioEntryFromDB(portfolioId);
			if (info.getCurrentCash() > portfolioInfoInDB.getCurrentCash()) {
				// current cash is going to go up
				createTransactionRecord = true;
				transInfo.setTransType("A"); // A - add, R - reduce
				transInfo.setPortfolioId(portfolioId);
				transInfo.setMDate(currentDate);
				transInfo.setCurrentCash(info.getCurrentCash());
				transInfo.setTransAmount(info.getCurrentCash()
						- portfolioInfoInDB.getCurrentCash());

			} else if (info.getCurrentCash() < portfolioInfoInDB
					.getCurrentCash()) {
				// current cash is going to go down
				createTransactionRecord = true;
				transInfo.setTransType("R"); // A - add, R - reduce
				transInfo.setPortfolioId(portfolioId);
				transInfo.setMDate(currentDate);
				transInfo.setCurrentCash(info.getCurrentCash());
				transInfo.setTransAmount(portfolioInfoInDB.getCurrentCash()
						- info.getCurrentCash());
			}

			if (createTransactionRecord) {
				// record the transaction record here
				// find the current last trans_id and then add 1
				int maxSeqInDB = portfolioDao
						.getMaxTransactionSeqFromDB(portfolioId);
				transInfo.setTransId(maxSeqInDB + 1);
				portfolioDao.addPortfolioTransactionToDB(transInfo);
			}

			// then update the portfolio_list table
			info.setPortfolioId(portfolioId);
			portfolioDao.updatePortfolioEntryToDB(info);

			// need to reset the cache
			DBCachePool.resetPortfolioListFromDB(portfolioVO.getUserId(),
					portfolioDao);
		}
	}

	/**
	 * Add a position to the db; and update the portfolio's cash status Need to
	 * see if cash is available to the new position
	 * 
	 * @param portfolioVO
	 * @param currentDate
	 * @throws Exception
	 */
	private int addPosition(PortfolioVO portfolioVO, String currentDate)
			throws Exception {
		String portfolioId = portfolioVO.getPortfolioId();
		PortfolioInfo portfolioInfo = portfolioVO.getPortfolioInfo();
		PositionInfo info = portfolioVO.getPositionInfo();
		info.setPortfolioId(portfolioId);

		// check in case
		if (info.getNumShares() <= 0) {
			System.out
					.println("PortfolioHandler: Number shares is 0 or negative: "
							+ info.getNumShares());
			return 0; // do not create anything; this should be blocked from
						// client side as well
		}

		// need to verify the stock
		String ticker = info.getTicker();
		if (ticker == null || ticker.equals("")) {
			System.out.println("PortfolioHandler: Ticker is not given: "
					+ info.getTicker());
			portfolioVO.setMessage("Ticker is not provided.");
			return 0; // ticker is not provided
		}

		ticker = ticker.toUpperCase();
		Vector stockList = stockDao.getStockListFromDB(null, ticker, null);
		if (stockList == null || stockList.size() == 0) {
			// ticker not found in our DB
			portfolioVO.setMessage("Ticker is not in our DB.");
			return 0; // for now

		} else if (stockList.size() == 1) {
			// only one is found in our DB
			info.setStockId(((Stock) stockList.get(0)).getStockId());
			info.setTicker(ticker);
		} else {
			// more than one is found, we need to present to user to select the
			// market
			// but for now, take the first one
			info.setStockId(((Stock) stockList.get(0)).getStockId());
			info.setTicker(ticker);
		}

		// order id = max(order id in DB) + 1
		int maxSeqInDB = portfolioDao.getMaxPositionSeqFromDB(portfolioId);
		info.setOrderId(maxSeqInDB + 1);
		info.setOpenDate(currentDate);
		info.setOrderStatus(PortfolioConstants.OPEN_POSITION);

		double tradeCost = portfolioInfo.getTradeCost();
		double openAmount = 0;
		double currentCashBalance = 0;
		// need to calculate the Amount = (open price * num_shares) +/-
		// trade_cost
		if (info.getTradeType() != null
				&& info.getTradeType().equals(PortfolioConstants.SHORT_TYPE)) {
			// short
			openAmount = info.getOpenPrice() * info.getNumShares() - tradeCost;
			openAmount = NumberUtil.formatDoubleTwo(openAmount);
			info.setOpenAmount(openAmount);
			currentCashBalance = portfolioInfo.getCurrentCash() + openAmount;

			// in rare case, currentCashBalance may <= 0, but should be blocked
			// in client side
		} else {
			// assume it is long position
			openAmount = info.getOpenPrice() * info.getNumShares() + tradeCost;
			openAmount = NumberUtil.formatDoubleTwo(openAmount);
			info.setOpenAmount(openAmount);
			currentCashBalance = portfolioInfo.getCurrentCash() - openAmount;

			// if long, need to see if the current cash is enough or not for the
			// new position
			if (info.getOpenAmount() > portfolioInfo.getCurrentCash()) {
				// that means cannot do the order like this, as there is not
				// enough money
				// this should be blocked from client side as well
				portfolioVO
						.setMessage("You do not have enough money to open the position.");
				return 0;
			}

		}

		// for long position, the current cash is going down; for short, the
		// current cash is going up
		currentCashBalance = NumberUtil.formatDoubleTwo(currentCashBalance);
		portfolioInfo.setCurrentCash(currentCashBalance);
		portfolioDao.updatePortfolioEntryToDB(portfolioInfo, true);

		// insert the position to the DB
		portfolioDao.addPortfolioPositionEntryToDB(info);

		return 1;

	}

	private void updatePosition(PortfolioVO portfolioVO, String currentDate)
			throws Exception {
		String portfolioId = portfolioVO.getPortfolioId();
		PortfolioInfo portfolioInfo = portfolioVO.getPortfolioInfo();
		PositionInfo info = portfolioVO.getPositionInfo();
		info.setPortfolioId(portfolioId);
		info.setCloseDate(currentDate);
		int orderId = info.getOrderId();

		// need to see if it is same num_shares or not
		// if same, then just need to close the position; if it is not same,
		// then open new position to
		// hold the remain shares and keep it open
		PositionInfo positionInfoInDB = portfolioDao
				.getPortfolioPositionEntryFromDB(orderId, portfolioId);
		if (info.getNumShares() >= positionInfoInDB.getNumShares()) {
			// close the position
			info.setOrderStatus(PortfolioConstants.CLOSED_POSITION);

			// need to update portfolio cash - if long, add back, if short, take
			// off
			if (positionInfoInDB.getTradeType().equals(
					PortfolioConstants.LONG_TYPE)) {
				double closeAmount = info.getClosePrice() * info.getNumShares()
						- portfolioInfo.getTradeCost();
				closeAmount = NumberUtil.formatDoubleTwo(closeAmount);
				info.setCloseAmount(closeAmount);
				double newCurrentCash = portfolioInfo.getCurrentCash()
						+ info.getCloseAmount();
				newCurrentCash = NumberUtil.formatDoubleTwo(newCurrentCash);
				portfolioInfo.setCurrentCash(newCurrentCash);
			} else {
				double closeAmount = info.getClosePrice() * info.getNumShares()
						+ portfolioInfo.getTradeCost();
				closeAmount = NumberUtil.formatDoubleTwo(closeAmount);
				info.setCloseAmount(closeAmount);
				double newCurrentCash = portfolioInfo.getCurrentCash()
						- info.getCloseAmount();
				newCurrentCash = NumberUtil.formatDoubleTwo(newCurrentCash);
				portfolioInfo.setCurrentCash(newCurrentCash);
			}

			info.setOrderStatus(PortfolioConstants.CLOSED_POSITION);
			portfolioDao.updatePortfolioPositionEntryToDB(info);
			portfolioDao.updatePortfolioEntryToDB(portfolioInfo, true);

		} else {
			// create new position
			int newOrderNumShares = positionInfoInDB.getNumShares()
					- info.getNumShares();
			double closedOpenAmount = positionInfoInDB.getOpenPrice()
					* info.getNumShares();
			closedOpenAmount = NumberUtil.formatDoubleTwo(closedOpenAmount); // need
																				// format
																				// after
																				// multiplication

			PositionInfo positionInfoToDB = new PositionInfo();
			positionInfoToDB.setOpenDate(positionInfoInDB.getOpenDate());
			positionInfoToDB.setTradeType(positionInfoInDB.getTradeType());
			positionInfoToDB.setStockId(positionInfoInDB.getStockId());
			positionInfoToDB.setOrderStatus(PortfolioConstants.OPEN_POSITION);
			positionInfoToDB.setPortfolioId(portfolioId);
			positionInfoToDB.setOpenPrice(positionInfoInDB.getOpenPrice());
			positionInfoToDB.setNumShares(newOrderNumShares); // changed to
																// remain shares
			positionInfoToDB.setOpenAmount(positionInfoInDB.getOpenAmount()
					- closedOpenAmount);
			// figure a new order id for it
			int maxSeqInDB = portfolioDao.getMaxPositionSeqFromDB(portfolioId);
			positionInfoToDB.setOrderId(maxSeqInDB + 1);
			portfolioDao.addPortfolioPositionEntryToDB(positionInfoToDB);

			// update the position to the DB
			info.setOrderStatus(PortfolioConstants.CLOSED_POSITION);
			info.setOpenAmount(closedOpenAmount);

			// update the cach in portfolio
			// need to update portfolio cash - if long, add back, if short, take
			// off
			if (positionInfoInDB.getTradeType().equals(
					PortfolioConstants.LONG_TYPE)) {
				double closeAmount = info.getClosePrice() * info.getNumShares()
						- portfolioInfo.getTradeCost();
				closeAmount = NumberUtil.formatDoubleTwo(closeAmount); // need
																		// format
																		// after
																		// multiplication
				info.setCloseAmount(closeAmount);
				double newCurrentCash = portfolioInfo.getCurrentCash()
						+ info.getCloseAmount();
				portfolioInfo.setCurrentCash(newCurrentCash);
			} else {
				double closeAmount = info.getClosePrice() * info.getNumShares()
						+ portfolioInfo.getTradeCost();
				closeAmount = NumberUtil.formatDoubleTwo(closeAmount); // need
																		// format
																		// after
																		// multiplication
				info.setCloseAmount(closeAmount);
				double newCurrentCash = portfolioInfo.getCurrentCash()
						- info.getCloseAmount();
				portfolioInfo.setCurrentCash(newCurrentCash);
			}

			portfolioDao.updatePortfolioPositionEntryToDB(info);
			portfolioDao.updatePortfolioEntryToDB(portfolioInfo, true);
		}
	}

	/**
	 * Go through the list to figure out the gain/loss for the paper trade. For
	 * open positions, use the current price to figure it; For closed positions,
	 * use the close amount/open amount
	 * 
	 * @param watchList
	 */
	private double calculateGain(Vector positionList) {
		double investValue = 0; // only do for open positions

		if (positionList == null) {
			return investValue;
		}

		for (int i = 0; i < positionList.size(); i++) {
			PositionInfo positionInfo = (PositionInfo) positionList.get(i);
			if (positionInfo.getOpenAmount() <= 0
					|| positionInfo.getNumShares() <= 0
					|| positionInfo.getCurrentPrice() <= 0) {
				// very strange, but do not do it

			} else if (positionInfo.getOrderStatus() != null
					&& positionInfo.getOrderStatus().equals(
							PortfolioConstants.CLOSED_POSITION)) {
				// this position is a closed position, use the close amount/open
				// amount
				double percent = 0;
				double gain = 0;
				if (positionInfo.getTradeType() != null
						&& positionInfo.getTradeType().equals(
								PortfolioConstants.SHORT_TYPE)) {
					// short position
					gain = positionInfo.getOpenAmount()
							- positionInfo.getCloseAmount();
					if (positionInfo.getCloseAmount() <= 0) {
						// we may no see this case, as the close Amount at list
						// >= trade_cost. However, do check in case
						percent = 1;
					} else {
						percent = (double) gain / positionInfo.getCloseAmount();
					}
				} else {
					// assume it is a long position
					gain = positionInfo.getCloseAmount()
							- positionInfo.getOpenAmount();
					percent = (double) gain / positionInfo.getOpenAmount();
				}

				percent *= 100;
				percent = NumberUtil.formatDoubleOne(percent);
				positionInfo.setGainPercent(percent);
				gain = NumberUtil.formatDoubleTwo(gain);
				positionInfo.setGain(gain);

			} else {
				// assume it is open position
				double percent = 0;
				double gain = 0;
				double currentAmount = 0;
				currentAmount = positionInfo.getCurrentPrice()
						* positionInfo.getNumShares();
				if (positionInfo.getTradeType() != null
						&& positionInfo.getTradeType().equals(
								PortfolioConstants.SHORT_TYPE)) {
					// short position
					gain = positionInfo.getOpenAmount() - currentAmount;
					percent = (double) gain / currentAmount;
					currentAmount = (-1) * currentAmount;
				} else {
					// assume it is a long position
					gain = currentAmount - positionInfo.getOpenAmount();
					percent = (double) gain / positionInfo.getOpenAmount();
				}

				percent *= 100;
				percent = NumberUtil.formatDoubleOne(percent);
				positionInfo.setGainPercent(percent);
				gain = NumberUtil.formatDoubleTwo(gain);
				positionInfo.setGain(gain);
				currentAmount = NumberUtil.formatDoubleTwo(currentAmount);
				positionInfo.setCurrentAmount(currentAmount);
				investValue += currentAmount;
			}
		}

		// investValue = NumberUtil.formatDoubleTwo(investValue);
		return investValue;
	}
}
