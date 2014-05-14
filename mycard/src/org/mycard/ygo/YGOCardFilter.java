package org.mycard.ygo;

import org.mycard.ygo.provider.YGOCards;

import android.database.Cursor;
import android.os.Bundle;

public class YGOCardFilter implements ICardFilter {
	
	private int mTypeIndex;
	
	private int mDetailType;
	
	private int mRaceIndex;
	private int mRace;
	
	private int mAttrIndex;
	private int mAttr;
	
	public YGOCardFilter() {
		reset();
	}
	
	private void reset() {
		mDetailType = CARD_FILTER_TYPE_MONSTER_ALL;
		mRace = CARD_FILTER_RACE_ALL;
		mAttr = CARD_FILTER_ATTR_ALL;
	}

	@Override
	public void onFilter(int type, int arg1, Bundle obj) {
		switch (type) {
		case CARD_FILTER_TYPE_ALL:
		case CARD_FILTER_MONSTER_TYPE:
		case CARD_FILTER_SPELL_TYPE:
		case CARD_FILTER_TRAP_TYPE:
			mTypeIndex = type;
			mDetailType = arg1;
			break;
		case CARD_FILTER_RACE:
			mRaceIndex = type;
			mRace = arg1;
			break;
		case CARD_FILTER_ATTR:
			mAttrIndex = type;
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
		if (!(mDetailType == CARD_FILTER_TYPE_MONSTER_ALL || (type & YGOArrayStore.sTypeMaps.get(mTypeIndex).get(mDetailType)) > 0)) {
			return false;
		}
		int race = dataCursor.getInt(YGOCards.COMMON_DATA_PROJECTION_RACE_INDEX);
		if (!(mRace == CARD_FILTER_RACE_ALL || (race & YGOArrayStore.sTypeMaps.get(mRaceIndex).get(mRace)) > 0)) {
			return false;
		}
		int attr = dataCursor.getInt(YGOCards.COMMON_DATA_PROJECTION_ATTR_INDEX);
		if (!(mAttr == CARD_FILTER_ATTR || (attr & YGOArrayStore.sTypeMaps.get(mAttrIndex).get(mAttr)) > 0)) {
			return false;
		}
		return true;
	}

	@Override
	public String buildSelection() {
		StringBuilder sb = new StringBuilder();
		if (mDetailType != CARD_FILTER_TYPE_MONSTER_ALL) {
			sb.append("(").append(YGOCards.Datas.TYPE).append("&")
			.append(YGOArrayStore.sTypeMaps.get(mTypeIndex).get(mDetailType)).append(" > 0) AND ")
			.append("(").append(YGOCards.Datas.TYPE).append("&")
			.append(YGOArrayStore.sTypeMaps.get(mTypeIndex).get(0)).append(" > 0) AND ");
		}
		if (mRace != CARD_FILTER_RACE_ALL) {
			sb.append("(").append(YGOCards.Datas.RACE).append("&")
			.append(YGOArrayStore.sTypeMaps.get(mRaceIndex).get(mRace)).append(" > 0) AND ");
		}
		if (mAttr != CARD_FILTER_ATTR_ALL) {
			sb.append("(").append(YGOCards.Datas.ATTRIBUTE).append("&")
			.append(YGOArrayStore.sTypeMaps.get(mAttrIndex).get(mAttr)).append(" > 0) AND ");
		}
		if (sb.length() > 5) {
			sb.delete(sb.length() - 5, sb.length());
		}
		return sb.length() == 0 ? null : sb.toString();
	}

}
