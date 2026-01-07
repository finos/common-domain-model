### Index Transition Business Event

####  Cross Currency Swap

Before Trade
- Vanilla swap created on `2018-03-19`:
    - Between parties `54930084UKLVMY22DS16` and `48750084UKLVTR22DS78`.
    - Trade Identifier `DRTY123456`
    - Notional of `1400 EUR`
    - Spread of `-0.0045`, floating rate index `EUR-EURIBOR-Telerate`, with index tenor `3M`
    - Spread of `0.0`, floating rate index `USD-LIBOR-BBA`, with index tenor `3M`

After Trade
- Index transition performed on `2018-06-17`:
    - Replace floating rate index `EUR-EURIBOR-Telerate` with `EUR-EURIBOR-Reuters`, and apply spread adjustment of `0.001`, resulting in spread of `-0.0035`
    - Replace floating rate index `USD-LIBOR-BBA` with `USD-LIBOR-ISDA`, and apply spread adjustment of `0.002`, resulting in spread of `0.002`
    - Effective date of `2018-06-19`
