package org.mycard.ygo;

import org.mycard.ygo.provider.YGOCards;

import android.database.Cursor;
import android.os.Bundle;

public class YGOCardFilter implements ICardFilter {
	
	private int mType;
	
	private int mDetailType;
	
	private int mRace;
	
	private int mAttr;
	
	public YGOCardFilter() {
		reset();
	}
	
	private void reset() {
		mType = CARD_FILTER_TYPE_ALL;
		mRace = CARD_FILTER_RACE_ALL;
		mAttr = CARD_FILTER_ATTR_ALL;
	}

	@Override
	public void onFilter(int type, int arg1, int arg2, Bundle obj) {
		switch (type) {
		case CARD_FILTER_TYPE:
			mType = arg1;
			mDetailType = arg2;
			break;
		case CARD_FILTER_RACE:
			mRace = arg1;
			break;
		case CARD_FILTER_ATTR:
			mAttr = arg1;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void resetFilter() {
		reset();
	}
	
	public boolean isCardFiltered(Cursor dataCursor) {
		int type = dataCursor.getInt(YGOCards.COMMON_DATA_PROJECTION_TYPE_INDEX);
		if (!(mType == CARD_FILTER_TYPE_ALL || (type & YGOArrayStore.sTypeMaps.get(mType).get(mDetailType)) > 0)) {
			return false;
		}
		int race = dataCursor.getInt(YGOCards.COMMON_DATA_PROJECTION_RACE_INDEX);
		if (!(mRace == CARD_FILTER_RACE_ALL || (race <<= mRace) == 1)) {
			return false;
		}
		int attr = dataCursor.getInt(YGOCards.COMMON_DATA_PROJECTION_ATTR_INDEX);
		if (!(mAttr == CARD_FILTER_ATTR || (attr <<= mAttr) == 1)) {
			return false;
		}
		return true;
	}

	@Override
	public String buildSelection() {
		StringBuilder sb = new StringBuilder();
		if (mType != CARD_FILTER_TYPE_ALL) {
			sb.append("(").append(YGOCards.Datas.TYPE).append("&")
			.append(YGOArrayStore.sTypeMaps.get(mType).get(mDetailType)).append(" > 0) AND ")
			.append("(").append(YGOCards.Datas.TYPE).append("&")
			.append(YGOArrayStore.sTypeMaps.get(mType).get(0)).append(" > 0) AND ");
		}
		if (mRace != CARD_FILTER_RACE_ALL) {
			sb.append("(").append(YGOCards.Datas.RACE).append("<<")
			.append(mRace-1).append(" = 1) AND ");
		}
		if (mAttr != CARD_FILTER_ATTR_ALL) {
			sb.append("(").append(YGOCards.Datas.ATTRIBUTE).append("<<")
			.append(mAttr-1).append(" = 1) AND ");
		}
		if (sb.length() > 5) {
			sb.delete(sb.length() - 5, sb.length());
		}
		return sb.length() == 0 ? null : sb.toString();
	}

}
