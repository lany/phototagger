/**
 * 
 */
package org.lizardbase.phototagger;

import java.io.StringWriter;

import org.json.JSONObject;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;



/**
 * @author Yu
 *
 */
public class TagInfo {
	protected static final String uri = "http://lizardbase.org/phototagger/";
	private static final String VER = "1.03"; //"1.0"
	
	protected static final String XMPHEAD = "<?xpacket begin='\uFEFF' id='W5M0MpCehiHzreSzNTczkc9d'?><x:xmpmeta xmlns:x=\"adobe:ns:meta/\">";
	protected static final String XMPTAIL = "</x:xmpmeta>";
	
	private static Model m = ModelFactory.createDefaultModel();

	public static final Property VERSION = m.createProperty(uri, "VERSION");
	public static final Property PDATE = m.createProperty(uri, "PDATE");
	public static final Property PSTARTTIME = m.createProperty(uri, "PSTARTTIME");
	public static final Property PENDTIME = m.createProperty(uri, "PENDTIME");
	public static final Property PSTARTTEMPERATURE = m.createProperty(uri, "PSTARTTEMPERATURE");
	public static final Property PSTARTHUMIDITY = m.createProperty(uri, "PSTARTHUMIDITY");
	public static final Property PENDTEMPERATURE = m.createProperty(uri, "PENDTEMPERATURE");
	public static final Property PENDHUMIDITY = m.createProperty(uri, "PENDHUMIDITY");
	public static final Property PCLOUDCOVER = m.createProperty(uri, "PCLOUDCOVER");
	public static final Property PWINDCONDITIONS = m.createProperty(uri, "PWINDCONDITIONS");
	public static final Property PSTUDYSITELOCATION = m.createProperty(uri, "PSTUDYSITELOCATION");
	public static final Property PLATITUDE = m.createProperty(uri, "PLATITUDE");
	public static final Property PLONGITUDE = m.createProperty(uri, "PLONGITUDE");
	public static final Property PLENGTHOFTRANSECT = m.createProperty(uri, "PLENGTHOFTRANSECT");
	public static final Property PURBANIZATION = m.createProperty(uri, "PURBANIZATION");
	public static final Property PVEGETATIONTYPES = m.createProperty(uri, "PVEGETATIONTYPES");
	public static final Property PSPECIES = m.createProperty(uri, "PSPECIES");
	public static final Property PHEIGHTABOVEGROUND = m.createProperty(uri, "PHEIGHTABOVEGROUND");
	public static final Property PRESTINGSPOTTMP = m.createProperty(uri, "PRESTINGSPOTTMP");
	public static final Property PBEHAVIOR = m.createProperty(uri, "PBEHAVIOR");
	public static final Property PCOMMENTS = m.createProperty(uri, "PCOMMENTS");
	public static final Property POBSERVERSINITIALS = m.createProperty(uri, "POBSERVERSINITIALS");
	public static final Property POBSERVEREMAIL = m.createProperty(uri, "POBSERVEREMAIL");
	public static final Property PAFFILIATEDSCHOOL = m.createProperty(uri, "PAFFILIATEDSCHOOL");
	
	private boolean isNew;
	
	private String pDate;
	private String pStartTime;
	private String pEndTime;
	private Double pStartTemperature;
	private Double pStartHumidity;
	private Double pEndTemperature;
	private Double pEndHumidity;
	private String pCloudCover;
	private String pWindConditions;
	private String pStudySiteLocations;
	private String pLatitude;
	private String pLongitude;
	private Double pLengthOfTransect;
	private String pUrbanization;
	private String pVegetationTypes;
	private String pSpecies;
	private Double pHeightAboveGround;
	private Double pRestingSpotTmp;
	private String pBehavior;
	private String pComments;
	private String pObserverInitials;
	private String pObserverEmail;
	private String pAffiliatedSchool;
	
	/**
	 * Constructor
	 */
	public TagInfo() {
		clear();
	}
	
	public void clear() {
		this.pDate = null;
		this.pStartTime = null;
		this.pEndTime = null;
		this.pStartTemperature = null;
		this.pEndTemperature = null;
		this.pStartHumidity = null;
		this.pEndHumidity = null;
		this.pCloudCover = null;
		this.pWindConditions = null;
		this.pStudySiteLocations = null;
		this.pLengthOfTransect = null;
		this.pUrbanization = null;
		this.pVegetationTypes = null;
		this.pSpecies = null;
		this.pHeightAboveGround = null;
		this.pRestingSpotTmp = null;
		this.pBehavior = null;
		this.pComments = null;
		this.pObserverInitials = null;
		this.pObserverEmail = null;
		this.pAffiliatedSchool = null;
		this.pLatitude = null;
		this.pLongitude = null;
	}

	public String getRDFString() {
		Model model = ModelFactory.createDefaultModel();
		Resource resource = model.getResource(TagInfo.getURI());
		
		resource.addProperty(TagInfo.VERSION, VER);
		if (this.getpDate() != null) {
			resource.addProperty(TagInfo.PDATE, this.getpDate());
		}
		if (this.getpStartTime() != null) {
			resource.addProperty(TagInfo.PSTARTTIME, this.getpStartTime().toString());
		}
		if (this.getpEndTime() != null) {
			resource.addProperty(TagInfo.PENDTIME, this.getpEndTime().toString());
		}
		if (this.getpStartTemperature() != null) {
			resource.addProperty(TagInfo.PSTARTTEMPERATURE, this.getpStartTemperature().toString());
		}
		if (this.getpEndTemperature() != null) {
			resource.addProperty(TagInfo.PENDTEMPERATURE, this.getpEndTemperature().toString());
		}
		if (this.getpStartHumidity() != null) {
			resource.addProperty(TagInfo.PSTARTHUMIDITY, this.getpStartHumidity().toString());
		}
		if (this.getpEndHumidity() != null) {
			resource.addProperty(TagInfo.PENDHUMIDITY, this.getpEndHumidity().toString());
		}
		if (this.getpCloudCover() != null) {
			resource.addProperty(TagInfo.PCLOUDCOVER, this.getpCloudCover().toString());
		}
		if (this.getpWindConditions() != null) {
			resource.addProperty(TagInfo.PWINDCONDITIONS, this.getpWindConditions().toString());
		}
		if (this.getpStudySiteLocations() != null) {
			resource.addProperty(TagInfo.PSTUDYSITELOCATION, this.getpStudySiteLocations().toString());
		}
		if (this.getpLengthOfTransect() != null) {
			resource.addProperty(TagInfo.PLENGTHOFTRANSECT, this.getpLengthOfTransect().toString());
		}
		if (this.getpUrbanization() != null) {
			resource.addProperty(TagInfo.PURBANIZATION, this.getpUrbanization().toString());
		}
		if (this.getpVegetationTypes() != null) {
			resource.addProperty(TagInfo.PVEGETATIONTYPES, this.getpVegetationTypes().toString());
		}
		if (this.getpSpecies() != null) {
			resource.addProperty(TagInfo.PSPECIES, this.getpSpecies().toString());
		}
		if (this.getpHeightAboveGround() != null) {
			resource.addProperty(TagInfo.PHEIGHTABOVEGROUND, this.getpHeightAboveGround().toString());
		}
		if (this.getpRestingSpotTmp() != null) {
			resource.addProperty(TagInfo.PRESTINGSPOTTMP, this.getpRestingSpotTmp().toString());
		}
		if (this.getpBehavior() != null) {
			resource.addProperty(TagInfo.PBEHAVIOR, this.getpBehavior().toString());
		}
		if (this.getpComments() != null) {
			resource.addProperty(TagInfo.PCOMMENTS, this.getpComments().toString());
		}
		if (this.getpObserverInitials() != null) {
			resource.addProperty(TagInfo.POBSERVERSINITIALS, this.getpObserverInitials().toString());
		}
		if (this.getpObserverEmail() != null) {
			resource.addProperty(TagInfo.POBSERVEREMAIL, this.getpObserverEmail().toString());
		}
		if (this.getpAffiliatedSchool() != null) {
			resource.addProperty(TagInfo.PAFFILIATEDSCHOOL, this.getpAffiliatedSchool().toString());
		}
		if (this.getpLatitude() != null) {
			resource.addProperty(TagInfo.PLATITUDE, this.getpLatitude().toString());
		}
		if (this.getpLongitude() != null) {
			resource.addProperty(TagInfo.PLONGITUDE, this.getpLongitude().toString());
		}
		
		StringWriter sw = new StringWriter();
		model.write(sw);
		return sw.toString();
	}
	
	public JSONObject getJSON() {
		JSONObject jsonObj = new JSONObject();
		
		try {
			if (this.getpDate() != null) {
				jsonObj.put(Const.TAG_DATE, this.getpDate());
			}
			if (this.getpStartTime() != null) {
				jsonObj.put(Const.TAG_STARTTIME, this.getpStartTime());
			}
			if (this.getpEndTime() != null) {
				jsonObj.put(Const.TAG_ENDTIME, this.getpEndTime());
			}
			if (this.getpStartTemperature() != null) {
				jsonObj.put(Const.TAG_STARTTMP, this.getpStartTemperature());
			}
			if (this.getpEndTemperature() != null) {
				jsonObj.put(Const.TAG_ENDTMP, this.getpEndTemperature());
			}
			if (this.getpStartHumidity() != null) {
				jsonObj.put(Const.TAG_STARTHUMIDITY, this.getpStartHumidity());
			}
			if (this.getpEndHumidity() != null) {
				jsonObj.put(Const.TAG_ENDHUMIDITY, this.getpEndHumidity());
			}
			if (this.getpCloudCover() != null) {
				jsonObj.put(Const.TAG_CLOUDCOVER, this.getpCloudCover());
			}
			if (this.getpWindConditions() != null) {
				jsonObj.put(Const.TAG_WINDCONDITIONS, this.getpWindConditions());
			}
			if (this.getpStudySiteLocations() != null) {
				jsonObj.put(Const.TAG_STUDYSITELOCATION, this.getpStudySiteLocations());
			}
			if (this.getpLengthOfTransect() != null) {
				jsonObj.put(Const.TAG_LENGTHOFTRANSECT, this.getpLengthOfTransect());
			}
			if (this.getpUrbanization() != null) {
				jsonObj.put(Const.TAG_URBANIZATION, this.getpUrbanization());
			}
			if (this.getpVegetationTypes() != null) {
				jsonObj.put(Const.TAG_VEGETATIONTYPES, this.getpVegetationTypes());
			}
			if (this.getpSpecies() != null) {
				jsonObj.put(Const.TAG_SPECIES, getSpeciesName(this.getpSpecies()));
			}
			if (this.getpHeightAboveGround() != null) {
				jsonObj.put(Const.TAG_HEIGHTABOVEGROUND, this.getpHeightAboveGround());
			}
			if (this.getpRestingSpotTmp() != null) {
				jsonObj.put(Const.TAG_RESTINGSPOTTMP, this.getpRestingSpotTmp());
			}
			if (this.getpBehavior() != null) {
				jsonObj.put(Const.TAG_BEHAVIOR, this.getpBehavior());
			}
			if (this.getpComments() != null) {
				jsonObj.put(Const.TAG_COMMENTS, this.getpComments());
			}
			if (this.getpObserverInitials() != null) {
				jsonObj.put(Const.TAG_OBSERVERINITIALS, this.getpObserverInitials());
			}
			if (this.getpObserverEmail() != null) {
				jsonObj.put(Const.TAG_OBSERVEREMAIL, this.getpObserverEmail());
			}
			if (this.getpAffiliatedSchool() != null) {
				jsonObj.put(Const.TAG_AFFILIATEDSCHOOL, this.getpAffiliatedSchool());
			}
			if (this.getpLatitude() != null) {
				jsonObj.put(Const.TAG_LATITUDE, this.getpLatitude());
			}
			if (this.getpLongitude() != null) {
				jsonObj.put(Const.TAG_LONGITUDE, this.getpLongitude());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return jsonObj;
	}
	
	private String getSpeciesName(String species) {
		if (species != null && species.equals(Const.ITEMS_SPECIES_VALUES[0])) {
			return Const.ITEMS_SPECIES_VALUES_DB[0].toString();
		} else if (species != null && species.equals(Const.ITEMS_SPECIES_VALUES[0])) {
			return Const.ITEMS_SPECIES_VALUES_DB[1].toString();
		} else {
			return "";
		}
	}
	
	public String getXMPString() {
		StringBuffer sb = new StringBuffer();
		sb.append(XMPHEAD).append(this.getRDFString()).append(XMPTAIL);
		return sb.toString();
	}
	
	/**
	 * returns the URI for this schema
	 * 
	 * @return the URI for this schema
	 */
	public static String getURI() {
		return uri;
	}

	private Double getDouble(String s) {
		if (s != null && !s.equals("")) {
			return new Double(s);
		} else {
			return null;
		}
	}

	/**
	 * @return the pDate
	 */
	public String getpDate() {
		return pDate;
	}

	/**
	 * @param pDate the pDate to set
	 */
	public void setpDate(String pDate) {
		this.pDate = pDate;
	}

	/**
	 * @return the pStartTime
	 */
	public String getpStartTime() {
		return pStartTime;
	}

	/**
	 * @param pStartTime the pStartTime to set
	 */
	public void setpStartTime(String pStartTime) {
		this.pStartTime = pStartTime;
	}

	/**
	 * @return the pEndTime
	 */
	public String getpEndTime() {
		return pEndTime;
	}

	/**
	 * @param pEndTime the pEndTime to set
	 */
	public void setpEndTime(String pEndTime) {
		this.pEndTime = pEndTime;
	}

	/**
	 * @return the pStartTemperature
	 */
	public String getpStartTemperature() {
		return pStartTemperature == null ? "" : String.valueOf(pStartTemperature);
	}

	/**
	 * @param pStartTemperature the pStartTemperature to set
	 */
	public void setpStartTemperature(Double pStartTemperature) {
		this.pStartTemperature = pStartTemperature;
	}

	/**
	 * @param pStartTemperature the pStartTemperature to set
	 */
	public void setpStartTemperature(String pStartTemperature) {
		this.pStartTemperature = getDouble(pStartTemperature);
	}

	/**
	 * @return the pStartHumidity
	 */
	public String getpStartHumidity() {
		return pStartHumidity == null ? "" : String.valueOf(pStartHumidity);
	}

	/**
	 * @param pStartHumidity the pStartHumidity to set
	 */
	public void setpStartHumidity(Double pStartHumidity) {
		this.pStartHumidity = pStartHumidity;
	}

	/**
	 * @param pStartHumidity the pStartHumidity to set
	 */
	public void setpStartHumidity(String pStartHumidity) {
		this.pStartHumidity = getDouble(pStartHumidity);
	}

	/**
	 * @return the pEndTemperature
	 */
	public String getpEndTemperature() {
		return pEndTemperature == null ? "" : String.valueOf(pEndTemperature);
	}

	/**
	 * @param pEndTemperature the pEndTemperature to set
	 */
	public void setpEndTemperature(Double pEndTemperature) {
		this.pEndTemperature = pEndTemperature;
	}

	/**
	 * @param pEndTemperature the pEndTemperature to set
	 */
	public void setpEndTemperature(String pEndTemperature) {
		this.pEndTemperature = getDouble(pEndTemperature);
	}

	/**
	 * @return the pEndHumidity
	 */
	public String getpEndHumidity() {
		return pEndHumidity == null ? "" : String.valueOf(pEndHumidity);
	}

	/**
	 * @param pEndHumidity the pEndHumidity to set
	 */
	public void setpEndHumidity(Double pEndHumidity) {
		this.pEndHumidity = pEndHumidity;
	}

	/**
	 * @param pEndHumidity the pEndHumidity to set
	 */
	public void setpEndHumidity(String pEndHumidity) {
		this.pEndHumidity = getDouble(pEndHumidity);
	}

	/**
	 * @return the pCloudCover
	 */
	public String getpCloudCover() {
		return pCloudCover;
	}

	/**
	 * @param pCloudCover the pCloudCover to set
	 */
	public void setpCloudCover(String pCloudCover) {
		this.pCloudCover = pCloudCover;
	}

	/**
	 * @return the pWindConditions
	 */
	public String getpWindConditions() {
		return pWindConditions;
	}

	/**
	 * @param pWindConditions the pWindConditions to set
	 */
	public void setpWindConditions(String pWindConditions) {
		this.pWindConditions = pWindConditions;
	}

	/**
	 * @return the pStudySiteLocations
	 */
	public String getpStudySiteLocations() {
		return pStudySiteLocations;
	}

	/**
	 * @param pStudySiteLocations the pStudySiteLocations to set
	 */
	public void setpStudySiteLocations(String pStudySiteLocations) {
		this.pStudySiteLocations = pStudySiteLocations;
	}

	/**
	 * @return the pLatitude
	 */
	public String getpLatitude() {
		return pLatitude;
	}

	/**
	 * @param pLatitude the pLatitude to set
	 */
	public void setpLatitude(String pLatitude) {
		this.pLatitude = pLatitude;
	}

	/**
	 * @return the pLongitude
	 */
	public String getpLongitude() {
		return pLongitude;
	}

	/**
	 * @param pLongitude the pLongitude to set
	 */
	public void setpLongitude(String pLongitude) {
		this.pLongitude = pLongitude;
	}

	/**
	 * @return the pLengthOfTransect
	 */
	public String getpLengthOfTransect() {
		return pLengthOfTransect == null ? "" : String.valueOf(pLengthOfTransect);
	}

	/**
	 * @param pLengthOfTransect the pLengthOfTransect to set
	 */
	public void setpLengthOfTransect(Double pLengthOfTransect) {
		this.pLengthOfTransect = pLengthOfTransect;
	}

	/**
	 * @param pLengthOfTransect the pLengthOfTransect to set
	 */
	public void setpLengthOfTransect(String pLengthOfTransect) {
		this.pLengthOfTransect = getDouble(pLengthOfTransect);
	}

	/**
	 * @return the pUrbanization
	 */
	public String getpUrbanization() {
		return pUrbanization;
	}

	/**
	 * @param pUrbanization the pUrbanization to set
	 */
	public void setpUrbanization(String pUrbanization) {
		this.pUrbanization = pUrbanization;
	}

	/**
	 * @return the pVegetationTypes
	 */
	public String getpVegetationTypes() {
		return pVegetationTypes;
	}

	/**
	 * @param pVegetationTypes the pVegetationTypes to set
	 */
	public void setpVegetationTypes(String pVegetationTypes) {
		this.pVegetationTypes = pVegetationTypes;
	}

	/**
	 * @return the pSpecies
	 */
	public String getpSpecies() {
		return pSpecies;
	}

	/**
	 * @param pSpecies the pSpecies to set
	 */
	public void setpSpecies(String pSpecies) {
		this.pSpecies = pSpecies;
	}

	/**
	 * @return the pHeightAboveGround
	 */
	public String getpHeightAboveGround() {
		return pHeightAboveGround == null ? "" : String.valueOf(pHeightAboveGround);
	}

	/**
	 * @param pHeightAboveGround the pHeightAboveGround to set
	 */
	public void setpHeightAboveGround(Double pHeightAboveGround) {
		this.pHeightAboveGround = pHeightAboveGround;
	}

	/**
	 * @param pHeightAboveGround the pHeightAboveGround to set
	 */
	public void setpHeightAboveGround(String pHeightAboveGround) {
		this.pHeightAboveGround = getDouble(pHeightAboveGround);
	}

	/**
	 * @return the pRestingSpotTmp
	 */
	public String getpRestingSpotTmp() {
		return pRestingSpotTmp == null ? "" : String.valueOf(pRestingSpotTmp);
	}

	/**
	 * @param pRestingSpotTmp the pRestingSpotTmp to set
	 */
	public void setpRestingSpotTmp(Double pRestingSpotTmp) {
		this.pRestingSpotTmp = pRestingSpotTmp;
	}

	/**
	 * @param pRestingSpotTmp the pRestingSpotTmp to set
	 */
	public void setpRestingSpotTmp(String pRestingSpotTmp) {
		this.pRestingSpotTmp = getDouble(pRestingSpotTmp);
	}

	/**
	 * @return the pBehavior
	 */
	public String getpBehavior() {
		return pBehavior;
	}

	/**
	 * @param pBehavior the pBehavior to set
	 */
	public void setpBehavior(String pBehavior) {
		this.pBehavior = pBehavior;
	}

	/**
	 * @return the pComments
	 */
	public String getpComments() {
		return pComments;
	}

	/**
	 * @param pComments the pComments to set
	 */
	public void setpComments(String pComments) {
		this.pComments = pComments;
	}

	/**
	 * @return the pObserverInitials
	 */
	public String getpObserverInitials() {
		return pObserverInitials;
	}

	/**
	 * @param pObserverInitials the pObserverInitials to set
	 */
	public void setpObserverInitials(String pObserverInitials) {
		this.pObserverInitials = pObserverInitials;
	}

	/**
	 * @return the pObserverEmail
	 */
	public String getpObserverEmail() {
		return pObserverEmail;
	}

	/**
	 * @param pObserverEmail the pObserverEmail to set
	 */
	public void setpObserverEmail(String pObserverEmail) {
		this.pObserverEmail = pObserverEmail;
	}

	/**
	 * @return the pAffiliatedSchool
	 */
	public String getpAffiliatedSchool() {
		return pAffiliatedSchool;
	}

	/**
	 * @param pAffiliatedSchool the pAffiliatedSchool to set
	 */
	public void setpAffiliatedSchool(String pAffiliatedSchool) {
		this.pAffiliatedSchool = pAffiliatedSchool;
	}

	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @param isNew the isNew to set
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

}
