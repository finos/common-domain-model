### Create Allocation Business Event

#### New Security Lending Transaction
- Execution is performed on `2020-9-21` between parties `KTKL (Agent Lender)` and `CP001 (Borrower)`
- `200000` shares of `ST001` are lent at `25 USD` with a lending fee of `1%` and a margin haircut of `2%`.

#### Allocation
- Lender internally allocates the trade on the same day across two funds, resulting in:
  - `after` trade between the agent lender `KTKL (Agent Lender)` and the fund `Fund 1`
    - allocated `120000` shares 
	- trade identifier set to `0266001-1`
	- Unique Transaction Identifier (UTI) set to `LEI12345ABCDE-20250922-TRADE001` as reportable under SFTR
  - `after` trade between the agent lender `KTKL (Agent Lender)` and the fund `Fund 2`
    - allocated `80000` shares
	- trade identifier set to `0266001-2`
	- Unique Transaction Identifier (UTI) set to `LEI12345ABCDE-20250922-TRADE002` as reportable under SFTR
- Initial Execution between `KTKL (Agent Lender)` and `CP001 (Borrower)` remains open as this is the agreement between the lender and the borrower
