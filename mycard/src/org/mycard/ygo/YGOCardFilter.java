package org.mycard.ygo;

import org.mycard.ygo.provider.YGOCards;

import android.os.Bundle;

public class YGOCardFilter implements ICardFilter {
	
	private int mTypeIndex;
	
	private int mDetailType;
	
	private int mRaceIndex;
	private int mRace;
	
	private int mAttrIndex;
	private int mAttr;
	
	private int mOT;
	
	private int mAtkMax;
	private int mAtkMin;
	
	private int mDefMax;
	private int mDefMin;
	
	public YGOCardFilter() {
		reset();
	}
	
	private void reset() {
		mDetailType = CARD_FILTER_TYPE_MONSTER_ALL;
		mRace = CARD_FILTER_RACE_ALL;
		mAttr = CARD_FILTER_ATTR_ALL;
		mOT = CARD_FILTER_OT_ALL;
		
		mAtkMax = CARD_FILTER_ATKDEF_DEF;
		mAtkMin = CARD_FILTER_ATKDEF_DEF;
		mDefMax = CARD_FILTER_ATKDEF_DEF;
		mDefMin = CARD_FILTER_ATKDEF_DEF;
	}

	@Override
	public void onFilter(int type, int arg1, int arg2, Bundle obj) {
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
		case CARD_FILTER_OT:
			mOT = arg1;
			break;
		case CARD_FILTER_ATK:
			mAtkMax = arg2;
			mAtkMin = arg1;
			break;
		case CARD_FILTER_DEF:
			mDefMax = arg2;
			mDefMin = arg1;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void resetFilter() {
		reset();
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
		if (mOT != CARD_FILTER_OT_ALL) {
			sb.append("(").append(YGOCards.Datas.OT).append("=")
			.append(mOT).append(") AND ");
		}
		if (mAtkMax != CARD_FILTER_ATKDEF_DEF && mAtkMin != CARD_FILTER_ATKDEF_DEF) {
			if (mAtkMax == mAtkMin) {
				sb.append("(").append(YGOCards.Datas.ATK).append("=")
				.append(mAtkMax).append(") AND ");
			} else {
				sb.append("(").append(YGOCards.Datas.ATK)
				.append(" BETWEEN ").append(mAtkMin).append(" AND ").append(mAtkMax).append(") AND ");;
			}
		}
		if (mDefMax != CARD_FILTER_ATKDEF_DEF && mDefMin != CARD_FILTER_ATKDEF_DEF) {
			if (mDefMax == mDefMin) {
				sb.append("(").append(YGOCards.Datas.DEF).append("=")
				.append(mDefMax).append(") AND ");
			} else {
				sb.append("(").append(YGOCards.Datas.DEF)
				.append(" BETWEEN ").append(mDefMin).append(" AND ").append(mDefMax).append(") AND ");;
			}
		}
		if (sb.length() > 5) {
			sb.delete(sb.length() - 5, sb.length());
		}
		return sb.length() == 0 ? null : sb.toString();
	}

}
