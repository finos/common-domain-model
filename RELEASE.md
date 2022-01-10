# *DSL Syntax - Usage of only-element and distinct with list operations*

_What is being released_

This release enables the use of existing keywords `only-element` and `distinct` with a list operation (e.g. `map`, `filter` etc).  Previously they had to be used in a separate function steps.  All applicable usages in the model have been updated.

_Example_

Previously list operations, such as `filter` or `map`, had to be in a separate function step from `only-element`.

```
 func GetDrivingLicence: 
    inputs:
        drivingLicences DrivingLicence (0..*)
        licenceNumber string (1..1)
    output:
        drivingLicence DrivingLicence (0..1)

    alias filteredDrivingLicences:
        drivingLicences
            filter [ item -> licenceNumber = licenceNumber ]

    set drivingLicence:
        filteredDrivingLicences only-element
```

Now `only-element` can be combined in the same expression.

```
 func GetDrivingLicence: 
    inputs:
        drivingLicences DrivingLicence (0..*)
        licenceNumber string (1..1)
    output:
        drivingLicence DrivingLicence (0..1)
 
    set drivingLicence:
        drivingLicences
            filter [ item -> licenceNumber = licenceNumber ]
            only-element
```

_Review Directions_

In the CDM Portal, select the Textual Browser, and search for usages such as `ExtractCounterpartyByRole` and `ExtractAncillaryPartyByRole`.


# *DSL Syntax - Set and Add keywords for assigning a list or appending to a list*

_What is being released_

The existing function syntax `assign-output` appends list items to any existing list items.  However, it is often necessary to replace the items of the list, and this requires a new syntax to differentiate the two use-cases.  New keywords `set` and `add` have been introduced for function output assignment, which replace the existing `assign-output` functionality.  
All functions have been updated to use the new syntax.

_Example_

```
 func AddDrivingLicenceToVehicleOwnership:
    inputs:
        vehicleOwnership VehicleOwnership (1..1)
        newDrivingLicence DrivingLicence (1..1)
    output:
        updatedVehicleOwnership VehicleOwnership (1..1)

    set updatedVehicleOwnership: vehicleOwnership
    
    // add newDrivingLicence to existing list of driving licences
    add updatedVehicleOwnership -> drivingLicence: newDrivingLicence
```

_Example_

``` 
 func UpdateDateOfRenewal:
    inputs:
        vehicleOwnership VehicleOwnership (1..1)
        newDateOfRenewal date (1..1)
    output:
        updatedVehicleOwnership VehicleOwnership (1..1)

    set updatedVehicleOwnership: vehicleOwnership
    
    // overwrite existing list with an updated list of driving licences
    set updatedVehicleOwnership -> drivingLicence: 
        vehicleOwnership -> drivingLicence
            map [ Create_DrivingLicence( 
                        item -> countryofIssuance,
                        item -> licenceNumber,
                        item -> dateofIssuance,
                        newDateOfRenewal,
                        item -> vehicleEntitlement,
                        item -> penaltyPoints ) ]
```

_Review Directions_

In the CDM Portal, select the Textual Browser, and review all functions.
