package fr.afcepf.al29.dionychus.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import fr.afcepf.al29.dionychus.business.itf.IBusinessInventaire;
import fr.afcepf.al29.dionychus.entity.Arome;
import fr.afcepf.al29.dionychus.entity.Cepage;
import fr.afcepf.al29.dionychus.entity.Region;
import fr.afcepf.al29.dionychus.entity.TypeVin;
import fr.afcepf.al29.dionychus.entity.Vin;

@Controller("vinManagedBean")
@Scope("request")
public class VinManagedBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Autowired
	private IBusinessInventaire proxyInventaire;

	private String vigneron;

	private String millesime;

	private List<TypeVin> typeVins;

	private Integer typeVin;

	private Double prix;

	private List<Arome> aromes;

	private String arome;

	private String cepage;

	private Integer region;

	private List<Region> regions;

	private Map<String, String> cepageMap = new HashMap<String, String>();

	private Map<String, String> aromeMap = new HashMap<String, String>();
	
	private List<Vin> vins;
	
	private List<Vin> vinsRecherche;
	
	public String rechercher() {
		vinsRecherche = proxyInventaire.getAllVinAromeCepage();
		List<Vin> selectVin = new ArrayList<>();

		if ((typeVin != null) && (typeVin != 0)) {
			for (Vin vin : vinsRecherche) {
				if(vin.getTypeVin().getIdTypeVin() != typeVin) {
					selectVin.add(vin);
				}
			}
			vinsRecherche.removeAll(selectVin);
			selectVin.clear();
		}
		
		if(!millesime.equals("")){
			for (Vin vin : vinsRecherche) {
				if(vin.getAnnee() != Integer.parseInt(millesime)){
					selectVin.add(vin);
				}
			}
			vinsRecherche.removeAll(selectVin);
			selectVin.clear();
		}

		if(prix != 0) {
			for (Vin vin : vinsRecherche) {
				if(vin.getPrix() < prix) {
					selectVin.add(vin);
				}
			}
			vinsRecherche.removeAll(selectVin);
			selectVin.clear();
		}
		
		if(region != 0) {
			for (Vin vin : vinsRecherche) {
				if(vin.getRegion().getIdRegion() != region) {
					selectVin.add(vin);
				}
			}
			vinsRecherche.removeAll(selectVin);
			selectVin.clear();
		}
		
		if(arome != null){
			boolean estPresent = false;
			String[] chaineAromes = arome.split(",");
			for (Vin vin : vinsRecherche) {
				for (String str : chaineAromes) {
					for (Arome aro : vin.getAromes()) {
						if(aro.getLibelle().equals(proxyInventaire.getAromeById(Integer.parseInt(str)).getLibelle())){
							estPresent = true;
						}
					}
				}
				if(!estPresent){
					selectVin.add(vin);	
				}
				estPresent = false;
			}
			vinsRecherche.removeAll(selectVin);
			selectVin.clear();
		}
		
		if(cepage != null){
			boolean estPresent = false;
			String[] chaineCepages = cepage.split(",");
			for (Vin vin : vinsRecherche) {
				for (String str : chaineCepages) {
					for (Cepage cep : vin.getCepages()) {
						if(cep.getLibelle().equals(proxyInventaire.getCepageById(Integer.parseInt(str)).getLibelle())){
							estPresent = true;
						}
					}
				}
				if(!estPresent){
					selectVin.add(vin);	
				}
				estPresent = false;
			}
			vinsRecherche.removeAll(selectVin);
			selectVin.clear();
		}

		for (Vin vin : vinsRecherche) {
			System.out.println(vin);	
		}
		
		if (vins == null){
			System.out.println("null dans rechercher");
		} else {
			System.out.println("pas null dans rechercher");
		}

		return null;
	}

	public List<TypeVin> getTypeVins() {
		return proxyInventaire.getAllTypeVin();
	}

	public void setTypeVins(List<TypeVin> typeVins) {
		this.typeVins = typeVins;
	}

	public Double getPrix() {
		return prix;
	}

	public void setPrix(Double prix) {
		this.prix = prix;
	}

	public List<Arome> getAromes() {
		List<Arome> test = proxyInventaire.getAllArome();
		for (Arome arome : test) {
			System.out.println(arome.getIdArome() + " " + arome.getLibelle());
		}	
		return test;
	}

	public void setAromes(List<Arome> aromes) {
		this.aromes = aromes;
	}

	public Integer getTypeVin() {
		return typeVin;
	}

	public void setTypeVin(Integer typeVin) {
		this.typeVin = typeVin;
	}

	public String getArome() {
		return arome;
	}

	public void setArome(String arome) {
		this.arome = arome;
	}

	public String getCepage() {
		return cepage;
	}

	public void setCepage(String cepage) {
		this.cepage = cepage;
	}

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public String getVigneron() {
		return vigneron;
	}

	public void setVigneron(String vigneron) {
		this.vigneron = vigneron;
	}

	public String getMillesime() {
		return millesime;
	}

	public void setMillesime(String millesime) {
		this.millesime = millesime;
	}

	public Map<String, String> getCepageMap() {
		List<Cepage> testCepage = proxyInventaire.getAllCepage();

		for (Cepage cepage : testCepage) {
			cepageMap.put(cepage.getLibelle(), cepage.getIdCepage().toString());
		}
		return cepageMap;
	}

	public void setCepageMap(Map<String, String> cepageMap) {
		this.cepageMap = cepageMap;
	}

	public Map<String, String> getAromeMap() {
		List<Arome> testArome = proxyInventaire.getAllArome();

		for (Arome arome : testArome) {
			aromeMap.put(arome.getLibelle(), arome.getIdArome().toString());
		}
		return aromeMap;
	}

	public void setAromeMap(Map<String, String> aromeMap) {
		this.aromeMap = aromeMap;
	}

	public List<Region> getRegions() {
		return proxyInventaire.getAllRegion();
	}

	public void setRegions(List<Region> regions) {
		this.regions = regions;
	}

	public List<Vin> getVins() {
		if(vinsRecherche == null){
			System.out.println("null");
			return proxyInventaire.getAllVin();
		} else {
			System.out.println("pas null");
			return vinsRecherche;
		}
		
	}

	public void setVins(List<Vin> vins) {
		this.vinsRecherche = vins;
	}

	public List<Vin> getVinsRecherche() {
		return vinsRecherche;
	}

	public void setVinsRecherche(List<Vin> vinsRecherche) {
		this.vinsRecherche = vinsRecherche;
	}
	
	

}
