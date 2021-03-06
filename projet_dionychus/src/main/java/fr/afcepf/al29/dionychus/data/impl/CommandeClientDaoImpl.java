package fr.afcepf.al29.dionychus.data.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import com.mysql.jdbc.Statement;

import fr.afcepf.al29.dionychus.data.itf.CommandeClientDaoItf;
import fr.afcepf.al29.dionychus.entity.CommandeClient;
import fr.afcepf.al29.dionychus.mapper.CommandeClientMapper;

public class CommandeClientDaoImpl implements CommandeClientDaoItf {

	JdbcTemplate jdbcTemplate;
	DataSource dataSource;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<CommandeClient> getAll() {
		String SQL = "SELECT cc.id_commande, cc.date_creation, cc.date_validation, cc.numero_reference, cc.id_acteur, cc.id_promotion, cc.id_statut_commande, sc.libelle FROM commande cc  INNER JOIN statut_commande sc WHERE cc.id_statut_commande = sc.id_statut_commande";
		return jdbcTemplate.query(SQL, new CommandeClientMapper());
	}

	@Override
	public List<CommandeClient> getCommandesByIdUtilisateur(Integer paramIdUtilisateur) {
		String SQL = "SELECT cc.id_commande, cc.date_creation, cc.date_validation, cc.numero_reference, cc.id_acteur, cc.id_promotion, cc.id_statut_commande, sc.libelle FROM commande cc INNER JOIN statut_commande sc WHERE cc.id_statut_commande = sc.id_statut_commande AND cc.id_acteur = ?";
		return jdbcTemplate.query(SQL, new Object[] { paramIdUtilisateur }, new CommandeClientMapper());
	}

	@Override
	public List<CommandeClient> getCommandesByIdStatutCommande(Integer paramIdStatutCommande) {
		String SQL = "SELECT cc.id_commande, cc.date_creation, cc.date_validation, cc.numero_reference, cc.id_acteur, cc.id_promotion, cc.id_statut_commande, sc.libelle FROM commande cc INNER JOIN statut_commande sc WHERE cc.id_statut_commande = sc.id_statut_commande AND cc.id_statut_commande = ?";
		return jdbcTemplate.query(SQL, new Object[] { paramIdStatutCommande }, new CommandeClientMapper());
	}

	@Override
	public CommandeClient addCommandeClient(CommandeClient paramCommandeClient) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		String SQL = "INSERT INTO `bdd_dionychus`.`commande` (`date_creation`, `id_promotion`, `date_validation`, `numero_reference`, `id_statut_commande`, `id_acteur`) VALUES (?,?,?,?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setDate(1, paramCommandeClient.getDateCreation());
				statement.setInt(2, paramCommandeClient.getPromotion().getIdPromotion());
				statement.setDate(3, paramCommandeClient.getDateValidation());
				statement.setString(4, paramCommandeClient.getNumeroReference());
				statement.setInt(5, paramCommandeClient.getStatutCommande().getIdStatutCommande());
				statement.setInt(6, paramCommandeClient.getUtilisateur().getIdActeur());
				return statement;
			}
		}, holder);
		paramCommandeClient.setIdCommande(holder.getKey().intValue());
		return paramCommandeClient;
	}

	@Override
	public void updateCommandeClient(CommandeClient paramCommandeClient) {
		String SQL = "UPDATE `bdd_dionychus`.`commande` SET `date_creation`=?, `id_promotion`=?, `date_validation`=?, `numero_reference`=?, `id_statut_commande`=?, `id_acteur`=? WHERE `id_commande`=?";
		jdbcTemplate.update(SQL, new Object[]{ paramCommandeClient.getDateCreation(),
				paramCommandeClient.getPromotion().getIdPromotion(), paramCommandeClient.getDateValidation(),
				paramCommandeClient.getNumeroReference(), paramCommandeClient.getStatutCommande().getIdStatutCommande(),
				paramCommandeClient.getUtilisateur().getIdActeur(), paramCommandeClient.getIdCommande() });
	}

	@Override
	public void deleteCommandeClient(Integer paramIdCommandeClient) {
		String SQL = "DELETE FROM `bdd_dionychus`.`commande` WHERE id_commande = ?";
		jdbcTemplate.update(SQL, new Object[] {paramIdCommandeClient});

	}

	@Override
	public CommandeClient getCommandeClientById(Integer paramIdCommandeClient) {
		String SQL = "SELECT cc.id_commande, cc.date_creation, cc.date_validation, cc.numero_reference, cc.id_acteur, cc.id_promotion, cc.id_statut_commande, sc.libelle FROM commande cc  INNER JOIN statut_commande sc WHERE cc.id_statut_commande = sc.id_statut_commande AND cc.id_commande = ?";
		return jdbcTemplate.queryForObject(SQL, new Object[]{paramIdCommandeClient}, new CommandeClientMapper());
	}

	@Override
	public CommandeClient addPanier(CommandeClient panier) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		String SQL = "INSERT INTO `bdd_dionychus`.`commande` (`date_creation`, `id_statut_commande`,`numero_reference`) VALUES (?,'1',?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setDate(1, panier.getDateCreation());
				statement.setString(2, panier.getNumeroReference());
				return statement;
			}
		}, holder);
		panier.setIdCommande(holder.getKey().intValue());
		return panier;
	}
	
	@Override
	public void updateTypeLivraison(CommandeClient paramCommandeClient, Integer paramIdTypeLivraison){
		String SQL = "UPDATE `bdd_dionychus`.`commande` SET `id_type_livraison`=? WHERE `id_commande`=?";
		jdbcTemplate.update(SQL, new Object[]{paramIdTypeLivraison, paramCommandeClient.getIdCommande()});
	}

	@Override
	public Double getTarifLivraisonByIdCommande(Integer idCommande) {
		String SQL = "SELECT tl.tarification FROM type_livraison tl INNER JOIN commande c ON tl.id_type_livraison = c.id_type_livraison WHERE c.id_commande = ?";
		return jdbcTemplate.queryForObject(SQL,new Object[]{idCommande} ,Double.class);
	}

	@Override
	public void updatePanierValider(CommandeClient paramCommandeClient) {
		String SQL = "UPDATE `bdd_dionychus`.`commande` SET `id_statut_commande`=? WHERE `id_commande`=?";
		jdbcTemplate.update(SQL, new Object[]{ paramCommandeClient.getStatutCommande().getIdStatutCommande(), paramCommandeClient.getIdCommande() });
	}

	@Override
	public void updatePanierRefUtilisateur(CommandeClient panierUtilisateur) {
		String SQL = "UPDATE `bdd_dionychus`.`commande` SET `id_acteur`=? WHERE `id_commande`=?";
		jdbcTemplate.update(SQL, new Object[]{ panierUtilisateur.getUtilisateur().getIdActeur(), panierUtilisateur.getIdCommande() });		
	}

	@Override
	public CommandeClient addPanierPostCommande(CommandeClient panier) {
		GeneratedKeyHolder holder = new GeneratedKeyHolder();
		String SQL = "INSERT INTO `bdd_dionychus`.`commande` (`date_creation`, `id_statut_commande`,`numero_reference`, `id_acteur` ) VALUES (?,'1',?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement statement = con.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setDate(1, panier.getDateCreation());
				statement.setString(2, panier.getNumeroReference());
				statement.setInt(3, panier.getUtilisateur().getIdActeur());
				return statement;
			}
		}, holder);
		panier.setIdCommande(holder.getKey().intValue());
		return panier;
	}


}
