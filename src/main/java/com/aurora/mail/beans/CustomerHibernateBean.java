package com.aurora.mail.beans;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Repository
public class CustomerHibernateBean {

    private Long id;
    private String address;
    private String zip;
    private String city;
    private Boolean frozen;
    private Boolean distanceCustomer;
    private BigDecimal fee;
    private String telephone;
    private Date dateOfBirth;
    private Date createdTimestamp;
    private Long languageId;

    private CountryBean country;
    /*	private RiskProfileBean					riskProfile;
    */
    private Set<CustomerAccessBean> access = new HashSet<CustomerAccessBean>(0);
    /*
        private Set<PreferenceBean>				preferences;
        private Set<CustomerSubscriptionBean>	subscriptions;
    */
    private CustomerPrivateBean priv;
    private CustomerCorporateBean corporate;
    /*
        private transient Set<VerificationBean>	verifications;
        private transient KYCFormBean			kyc;
    */
    private transient String sessionPassword;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getFrozen() {
        return frozen;
    }

    public boolean isFrozen() {
        return (frozen != null) ? frozen.booleanValue() : false;
    }

    public void setFrozen(Boolean frozen) {
        this.frozen = frozen;
    }

    public Boolean getDistanceCustomer() {
        return distanceCustomer;
    }

    public void setDistanceCustomer(Boolean distanceCustomer) {
        this.distanceCustomer = distanceCustomer;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Date createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }


    public CountryBean getCountry() {
        return country;
    }

    public void setCountry(CountryBean country) {
        this.country = country;
    }
/*
    public RiskProfileBean getRiskProfile() {
		return riskProfile;
	}

	public void setRiskProfile(RiskProfileBean riskProfile) {
		this.riskProfile = riskProfile;
	}
*/

    public Set<CustomerAccessBean> getAccess() {
        return access;
    }

    public void setAccess(Set<CustomerAccessBean> access) {
        this.access = access;
    }

    public CustomerPrivateBean getPriv() {
        return priv;
    }

    public void setPriv(CustomerPrivateBean priv) {
        this.priv = priv;
    }

    public CustomerCorporateBean getCorporate() {
        return corporate;
    }

    public void setCorporate(CustomerCorporateBean corporate) {
        this.corporate = corporate;
    }
/*
    public Set<PreferenceBean> getPreferences() {
		return preferences;
	}

	public void setPreferences(Set<PreferenceBean> preferences) {
		this.preferences = preferences;
	}

	public Set<VerificationBean> getVerifications() {
		return verifications;
	}

	public void setVerifications(Set<VerificationBean> verifications) {
		this.verifications = verifications;
	}

	public Set<CustomerSubscriptionBean> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(Set<CustomerSubscriptionBean> subscriptions) {
		this.subscriptions = subscriptions;
	}

	public KYCFormBean getKyc() {
		return kyc;
	}

	public void setKyc(KYCFormBean kyc) {
		this.kyc = kyc;
	}

	public Map<String, Integer> getAccountStatus() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		if (verifications != null) {
			for (VerificationBean vb : verifications) {
				if (!vb.isValid())
					continue;

				// Verification type
				String verificationType = vb.getVerificationType().getVerificationType().getType().toLowerCase();

				// If verificationlevel is -2 (undecided) we will show it as 0 (pending)
				int verificationLevel = (vb.getVerificationLevel().getId() == -2) ? 0 : vb.getVerificationLevel().getId();

				// If any bank account is verified that one will show in this map rather than unverified
				if (vb.getVerificationType().equals(VerificationType.BANKACCOUNT) && map.get("bankaccount") != null && map.get("bankaccount") > 0)
					continue;

				map.put(verificationType, verificationLevel);

			}
		}
		return map;
	}

	public List<VerificationBean> getApprovedVerifications() {
		List<VerificationBean> list = new ArrayList<VerificationBean>();
		if (verifications != null) {
			for (VerificationBean vb : verifications) {
				if (vb.getVerificationLevel().getId() > 0)
					list.add(vb);
			}
		}
		return list;
	}

	public boolean canPlaceBuyOrders() {
		Map<String, Integer> verifications = getAccountStatus();
		// {identity=1, bankaccount=1, address=0, telephone=-1}

		int identity = (verifications.get(VerificationType.IDENTITY.getType().toLowerCase()) != null) ? verifications.get(VerificationType.IDENTITY.getType().toLowerCase()) : -3;
		int address = (verifications.get(VerificationType.ADDRESS.getType().toLowerCase()) != null) ? verifications.get(VerificationType.ADDRESS.getType().toLowerCase()) : -3;
		int jumio = (verifications.get(VerificationType.JUMIO.getType().toLowerCase()) != null) ? verifications.get(VerificationType.JUMIO.getType().toLowerCase()) : -3;

		return ((identity > 0 && address > 0) || jumio > 0);
	}

	public boolean canPlaceSellOrders() {
		List<VerificationBean> verifications = getApprovedVerifications();
		Iterator<VerificationBean> iter = verifications.iterator();
		boolean identity = false, address = false, bankaccount = false;
		while (iter.hasNext() || (identity && address && bankaccount && iter.hasNext())) {
			VerificationBean verBean = iter.next();
			if (verBean.getVerificationType().equals(VerificationType.ADDRESS) && verBean.getVerificationLevel().getId() > 0) {
				address = true;
			} else if (verBean.getVerificationType().equals(VerificationType.IDENTITY) && verBean.getVerificationLevel().getId() > 0) {
				identity = true;
			} else if (verBean.getVerificationType().equals(VerificationType.BANKACCOUNT) && verBean.getVerificationLevel().getId() > 0) {
				bankaccount = true;
			}
		}
		return (address && identity);
	}

	public boolean canPlaceDirectPaymentOrders() {
		Map<String, Integer> verifications = getAccountStatus();
		int identity = (verifications.get(VerificationType.IDENTITY.getType().toLowerCase()) != null) ? verifications.get(VerificationType.IDENTITY.getType().toLowerCase()) : -3;
		int address = (verifications.get(VerificationType.ADDRESS.getType().toLowerCase()) != null) ? verifications.get(VerificationType.ADDRESS.getType().toLowerCase()) : -3;
		return (address > 0 && identity > 0);
	}

	public boolean hasWalletSubscription() {
		boolean hasWallet = false;
		if(subscriptions != null) {
			for (CustomerSubscriptionBean subscription : subscriptions) {
				if (subscription.getSubscriptionService().getSubscriptionService().equals(SubscriptionService.WALLET) && subscription.isActive()) {
					hasWallet = true;
					break;
				}
			}
		}
		return hasWallet;
	}*/

    public boolean isCorporateCustomer() {
        return corporate != null;
    }

    public boolean isPrivateCustomer() {
        return priv != null;
    }

/*	public CustomerStatusBean getCustomerStatus() {
        return new CustomerStatusBean();
	}*/

    public String getSessionPassword() {
        return sessionPassword;
    }

    public void setSessionPassword(String sessionPassword) {
        this.sessionPassword = sessionPassword;
    }
/*
	public class CustomerStatusBean {
        public Account account = new Account();
        public Identity identity = new Identity();
        public Address address = new Address();
        public List<BankAccount> bank_accounts = new ArrayList<BankAccount>();
        public KYCFormBean kyc = getKyc();

        public CustomerStatusBean() {
            for (VerificationBean vb : getVerifications()) {
                VerificationTypeBean verificationType = vb.getVerificationType();
                if (verificationType.equals(VerificationType.BANKACCOUNT)) {
                    if (vb.getBankAccountBean() != null)
                        bank_accounts.add(new BankAccount(vb.getBankAccountBean(), vb.getVerificationLevel().getId()));
                } else if (verificationType.equals(VerificationType.ADDRESS)) {
                    address.status = vb.getVerificationLevel().getId();
                } else if (verificationType.equals(VerificationType.IDENTITY)) {
                    identity.status = vb.getVerificationLevel().getId();
                    if (identity.verification_method == null || identity.verification_method.isEmpty()) {
                        identity.verification_method = "document_upload";
                    }
                } else if (verificationType.equals(VerificationType.TELEPHONE)) {
                    account.phone_status = vb.getVerificationLevel().getId();
                } else if (verificationType.equals(VerificationType.CSIGN) && vb.getVerificationLevel().getId() > 0) {
                    identity.verification_method = "csign";
                }
            }
        }*/

    public class Account {
        Integer status;
        String email;
        Integer email_status = 0;
        String phone;
        Integer phone_status = 0;

        public Account() {
            email = getAccess().iterator().next().getCredentials().getEmail();
            email_status = getAccess().iterator().next().getCredentials().getActive() ? 1 : 0;
            phone = getTelephone();
        }

    }

/*		public class Identity {
			Integer	status	= 0;
			String	personal_number, first_name, last_name, dob, verification_method;

			public Identity() {
				personal_number = getPriv().getPersonalNumber();
				first_name = getPriv().getFirstName();
				last_name = getPriv().getLastName();
				if (getDateOfBirth() != null) {
					dob = ToolBox.getSimpleDate(getDateOfBirth());
				}
				// When we add different subclasses to verification bean
				// for (VerificationBean vb : getVerifications()) {
				// if vb.getVerificationType().equals(VerificationType.IDENTITY))
				// data.verification_method = vb.getCsign().getDescription();
				// data.verification_method = vb.getJumio().getDescription();
				// data.verification_method = vb.getEtc().getDescription();
				// }

			}
		}

		public class Address {
			Integer	status	= 0;
			String	street, zip, city, country, countrycode;

			public Address() {
				street = getAddress();
				zip = getZip();
				city = getCity();
				country = getCountry().getCountryEng();
				countrycode = getCountry().getCountryCode();
			}

		}

		public class BankAccount {
			Integer	status	= 0;
			String	routing_number, account_number, name, countryCode;

			public BankAccount(CustomerBankAccountBean bab, int status) {
				this.status = status;
				if (status != 0) {
					this.name = bab.getBank().getBankName();
					this.countryCode = bab.getBank().getCountryCode();
				}
				this.account_number = bab.getAccountNumber();
				this.routing_number = bab.getRoutingAddress();
			}

		}*/

}
